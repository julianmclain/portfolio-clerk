package persistence.tables

import models.Transaction
import org.joda.money.BigMoney
import play.api.db.slick.HasDatabaseConfigProvider
import slick.lifted.ProvenShape

import java.time.OffsetDateTime

trait TransactionTableDefinition {
  this: HasDatabaseConfigProvider[ApplicationPostgresProfile] =>

  import profile.api._

  class TransactionTable(tag: Tag)
      extends Table[Transaction](tag, "transactions")
      with AutoIncIdColumn
      with BaseTableDefinition {
    def portfolioId: Rep[Long] = column[Long]("portfolio_id")
    def portfolioAssetId: Rep[Long] = column[Long]("portfolio_asset_id")
    def quantity: Rep[BigDecimal] = column[BigDecimal]("quantity")
    def unitPrice: Rep[BigMoney] = column[BigMoney]("unit_price")
    def totalValue: Rep[BigMoney] = column[BigMoney]("total_value")
    def transactionDatetime: Rep[OffsetDateTime] =
      column[OffsetDateTime]("transaction_datetime")

    def * : ProvenShape[Transaction] =
      (
        id,
        portfolioId,
        portfolioAssetId,
        quantity,
        unitPrice,
        totalValue,
        transactionDatetime,
        createdAt,
        updatedAt
      ) <> ((Transaction.apply _).tupled, Transaction.unapply)
  }

}
