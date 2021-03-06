package persistence.tables

import models.Withdrawal
import org.joda.money.BigMoney
import persistence.ApplicationPostgresProfile
import play.api.db.slick.HasDatabaseConfigProvider
import slick.lifted.ProvenShape

import java.time.OffsetDateTime

trait WithdrawalTableDefinition {
  this: HasDatabaseConfigProvider[ApplicationPostgresProfile] =>

  import profile.api._

  class WithdrawalTable(tag: Tag)
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

  def withdrawals: TableQuery[WithdrawalTable] = TableQuery[WithdrawalTable]

}
