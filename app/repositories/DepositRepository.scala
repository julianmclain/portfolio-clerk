package repositories

import db.ApplicationPostgresProfile
import models.Deposit
import models.Money
import play.api.db.slick.DatabaseConfigProvider
import slick.lifted.ProvenShape

import javax.inject.Inject
import javax.inject.Singleton
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import play.api.db.slick.HasDatabaseConfigProvider

import java.time.OffsetDateTime

@Singleton
class DepositRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends HasDatabaseConfigProvider[ApplicationPostgresProfile] {

  import ApplicationPostgresProfile.api._

  private class DepositTable(tag: Tag) extends Table[Deposit](tag, "deposits") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def portfolioId: Rep[Long] = column[Long]("portfolio_id")
    def totalAmount: Rep[Money] = column[Money]("total_amount")
    def depositDateTime: Rep[OffsetDateTime] = {
      column[OffsetDateTime]("deposit_datetime")
    }
//    def createdAt: Rep[OffsetDateTime] = column[OffsetDateTime]("created_at")
//    def updatedAt: Rep[OffsetDateTime] = column[OffsetDateTime]("updated_at")

    def * : ProvenShape[Deposit] =
      (
        id,
        portfolioId,
        totalAmount,
        depositDateTime
//        createdAt,
//        updatedAt
      ) <> ((Deposit.apply _).tupled, Deposit.unapply)
  }

  private val deposits = TableQuery[DepositTable]

  def findById(id: Long): Future[Option[Deposit]] =
    db.run(deposits.filter(_.id === id).result.headOption)

  def create(deposit: Deposit): Future[Deposit] = {
    println("at least trying")
    val insertQuery = deposits returning deposits.map(_.id) into ((_, id) =>
      deposit.copy(id = id)
    )
    val action = insertQuery += deposit
    db.run(action)
  }

}
