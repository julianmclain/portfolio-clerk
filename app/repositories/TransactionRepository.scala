package repositories

import db.ApplicationPostgresProfile
import db.AutoIncIdColumn
import db.TimestampColumns
import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import slick.lifted.ProvenShape

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import models.Transaction
import org.joda.money.BigMoney
import play.api.db.slick.HasDatabaseConfigProvider

import java.time.OffsetDateTime

@Singleton
class TransactionRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends HasDatabaseConfigProvider[ApplicationPostgresProfile] {

  import ApplicationPostgresProfile.api._

  private class TransactionTable(tag: Tag)
      extends Table[Transaction](tag, "transactions")
      with AutoIncIdColumn
      with TimestampColumns {
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
