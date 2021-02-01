package repositories

import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import models.Transaction
import models.AssetType
import models.MoneyWrapper

@Singleton
final class TransactionRepository @Inject() (
    dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  import CustomColumnTypes.assetTypeMapper
  import CustomColumnTypes.moneyMapper

  private class TransactionTable(tag: Tag)
      extends Table[Transaction](tag, "transactions") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def assetId: Rep[Long] = column[Long]("asset_id")
    def portfolioId: Rep[Long] = column[Long]("portfolio_id")
    def assetName: Rep[String] = column[String]("asset_name")
    def assetSymbol: Rep[String] = column[String]("asset_symbol")
    def assetType: Rep[AssetType] = column[AssetType]("asset_type")
    def quantity: Rep[Int] = column[Int]("quantity")
    def unitPrice: Rep[MoneyWrapper] = column[MoneyWrapper]("unit_price")
    def totalValue: Rep[MoneyWrapper] = column[MoneyWrapper]("total_value")

    def * : ProvenShape[Transaction] =
      (
        id,
        assetId,
        portfolioId,
        assetName,
        assetSymbol,
        assetType,
        quantity,
        unitPrice,
        totalValue
      ) <> ((Transaction.apply _).tupled, Transaction.unapply)
  }
}
