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

@Singleton
class TransactionRepository @Inject() (
    dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class TransactionTable(tag: Tag)
      extends Table[Transaction](tag, "transactions") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def assetType: Rep[AssetType] =
      column[AssetType]("asset_type") // TODO - figure out how to save to DB
    def assetName: Rep[String] = column[String]("asset_name")
    def assetTickerSymbol: Rep[String] = column[String]("asset_ticker_symbol")
    def quantity: Rep[Int] = column[Int]("quantity")
    def unitPrice: Rep[Long] = column[Long]("unit_price")

    def * : ProvenShape[Transaction] =
      (
        id,
        assetType,
        assetName,
        assetTickerSymbol,
        quantity,
        unitPrice
      ).mapTo[Transaction]
  }
}
