package repositories

import models.Deposit
import models.MoneyWrapper
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import javax.inject.Inject
import javax.inject.Singleton
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

@Singleton
final class DepositRepository @Inject() (
    dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  import repositories.CustomColumnTypes.moneyMapper

  private class DepositTable(tag: Tag) extends Table[Deposit](tag, "deposits") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def totalAmount: Rep[MoneyWrapper] = column[MoneyWrapper]("total_amount")
    def portfolioId: Rep[Long] = column[Long]("portfolio_id")

    def * : ProvenShape[Deposit] =
      (
        id,
        totalAmount,
        portfolioId
      ) <> ((Deposit.apply _).tupled, Deposit.unapply)
  }

  private val deposits = TableQuery[DepositTable]

  def findById(id: Long): Future[Option[Deposit]] =
    db.run(deposits.filter(_.id === id).result.headOption)

  def create(deposit: Deposit): Future[Deposit] = {
    val insertQuery = deposits returning deposits.map(_.id) into ((_, id) =>
      deposit.copy(id = id)
    )
    val action = insertQuery += deposit
    db.run(action)
  }

}
