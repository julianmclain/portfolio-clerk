package persistence.tables

import models.Deposit
import org.joda.money.BigMoney
import persistence.ApplicationPostgresProfile
import play.api.db.slick.HasDatabaseConfigProvider
import slick.lifted.ProvenShape

import java.time.OffsetDateTime

trait DepositTableDefinition {
  this: HasDatabaseConfigProvider[ApplicationPostgresProfile] =>

  import profile.api._

  class DepositTable(tag: Tag)
      extends Table[Deposit](tag, "deposits")
      with AutoIncIdColumn
      with TimestampColumns {
    def portfolioId: Rep[Long] = column[Long]("portfolio_id")
    def totalAmount: Rep[BigMoney] = column[BigMoney]("total_amount")
    def depositDatetime: Rep[OffsetDateTime] = {
      column[OffsetDateTime]("deposit_datetime")
    }

    def * : ProvenShape[Deposit] =
      (
        id,
        portfolioId,
        totalAmount,
        depositDatetime,
        createdAt,
        updatedAt
      ) <> ((Deposit.apply _).tupled, Deposit.unapply)
  }

  def deposits: TableQuery[DepositTable] = TableQuery[DepositTable]
}
