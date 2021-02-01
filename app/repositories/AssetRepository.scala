package repositories

import models.Asset
import models.AssetType
import models.MoneyWrapper
import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
final class AssetRepository @Inject() (
    dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  import CustomColumnTypes.moneyMapper
  import CustomColumnTypes.assetTypeMapper

  private class AssetTable(tag: Tag) extends Table[Asset](tag, "assets") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def portfolioId: Rep[Long] = column[Long]("portfolio_id")
    def assetName: Rep[String] = column[String]("asset_name")
    def assetSymbol: Rep[String] = column[String]("asset_symbol")
    def assetType: Rep[AssetType] = column[AssetType]("asset_type")
    def quantity: Rep[BigDecimal] = column[BigDecimal]("quantity")
    def unitPrice: Rep[MoneyWrapper] = column[MoneyWrapper]("unit_price")
    def totalValue: Rep[MoneyWrapper] = column[MoneyWrapper]("total_value")

    def * : ProvenShape[Asset] =
      (
        id,
        portfolioId,
        assetName,
        assetSymbol,
        assetType,
        quantity,
        unitPrice,
        totalValue
      ) <> ((Asset.apply _).tupled, Asset.unapply)
  }

  private val assets = TableQuery[AssetTable]

  def findById(id: Long): Future[Option[Asset]] =
    db.run(assets.filter(_.id === id).result.headOption)

  def create(asset: Asset): Future[Asset] = {
    val insertQuery =
      assets returning assets.map(_.id) into ((_, id) => asset.copy(id = id))
    val action = insertQuery += asset
    db.run(action)
  }
}
