package repositories

import db.ApplicationPostgresProfile
import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import slick.lifted.ProvenShape

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import models.Transaction
import models.Money
import play.api.db.slick.HasDatabaseConfigProvider

@Singleton
class TransactionRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends HasDatabaseConfigProvider[ApplicationPostgresProfile] {

  import ApplicationPostgresProfile.api._

  private class TransactionTable(tag: Tag)
      extends Table[Transaction](tag, "transactions") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def portfolioId: Rep[Long] = column[Long]("portfolio_id")
    def portfolioAssetId: Rep[Long] = column[Long]("portfolio_asset_id")
    def quantity: Rep[BigDecimal] = column[BigDecimal]("quantity")
    def unitPrice: Rep[Money] = column[Money]("unit_price")
    def totalValue: Rep[Money] = column[Money]("total_value")

    def * : ProvenShape[Transaction] =
      (
        id,
        portfolioId,
        portfolioAssetId,
        quantity,
        unitPrice,
        totalValue
      ) <> ((Transaction.apply _).tupled, Transaction.unapply)
  }
}
