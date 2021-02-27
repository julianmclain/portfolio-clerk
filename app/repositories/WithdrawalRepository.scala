package repositories

import db.ApplicationPostgresProfile
import db.AutoIncIdColumn
import db.TimestampColumns
import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import slick.lifted.ProvenShape
import models.Portfolio
import models.Withdrawal
import org.joda.money.BigMoney
import play.api.db.slick.HasDatabaseConfigProvider

import java.time.OffsetDateTime
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
class WithdrawalRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends HasDatabaseConfigProvider[ApplicationPostgresProfile] {
  import ApplicationPostgresProfile.api._

  private class WithdrawalTable(tag: Tag)
      extends Table[Withdrawal](tag, "withdrawals")
      with AutoIncIdColumn
      with TimestampColumns {
    def portfolioId: Rep[Long] = column[Long]("portfolio_id")
    def totalAmount: Rep[BigMoney] = column[BigMoney]("total_amount")
    def withdrawalDatetime: Rep[OffsetDateTime] = {
      column[OffsetDateTime]("deposit_datetime")
    }
    def * : ProvenShape[Withdrawal] =
      (
        id,
        portfolioId,
        totalAmount,
        withdrawalDatetime,
        createdAt,
        updatedAt
      ) <> ((Withdrawal.apply _).tupled, Withdrawal.unapply)
  }

  private val withdrawals = TableQuery[WithdrawalTable]

  def findById(id: Long): Future[Option[Withdrawal]] =
    db.run(withdrawals.filter(_.id === id).result.headOption)

  def create(withdrawal: Withdrawal): Future[Withdrawal] = {
    val insertQuery =
      withdrawals returning withdrawals.map(_.id) into ((record, id) =>
        withdrawal.copy(
          id = id,
          updatedAt = record.updatedAt,
          createdAt = record.createdAt
        )
      )
    val action = insertQuery += withdrawal
    db.run(action)
  }
}
