package repositories

import db.ApplicationPostgresProfile
import db.ApplicationPostgresProfile.api._
import db.TimestampColumns
import models.Asset
import models.AssetSymbol
import models.Portfolio
import models.PortfolioAsset
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.lifted.ForeignKeyQuery
import slick.lifted.ProvenShape

import javax.inject.Inject
import javax.inject.Singleton
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
class PortfolioAssetRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends HasDatabaseConfigProvider[ApplicationPostgresProfile] {

  private val portfolioAssets = TableQuery[PortfolioAssetTable]
  private val assets = TableQuery[AssetTable]

  def findAssetQuantities(
      portfolioId: Long
  ): Future[Seq[(AssetSymbol, BigDecimal)]] = {
    val query = for {
      portfolioAsset <- portfolioAssets
      if portfolioAsset.portfolioId === portfolioId
      asset <- portfolioAsset.asset
    } yield (asset.assetSymbol, portfolioAsset.quantity)

    db.run(query.result)
  }

  def create(portfolioAsset: PortfolioAsset): Future[PortfolioAsset] = {
    val insertQuery =
      portfolioAssets returning portfolioAssets into ((record, id) =>
        PortfolioAsset(
          record.portfolioId,
          record.assetId,
          record.quantity,
          record.createdAt,
          record.updatedAt
        )
      )
    val action = insertQuery += portfolioAsset
    db.run(action)
  }
}

private[repositories] class PortfolioAssetTable(tag: Tag)
    extends Table[PortfolioAsset](tag, "portfolio_assets")
    with TimestampColumns {

  private val assets = TableQuery[AssetTable]
  private val portfolios = TableQuery[PortfolioTable]

  def portfolioId: Rep[Long] = column[Long]("portfolio_id")
  def assetId: Rep[Long] = column[Long]("asset_id")
  def quantity: Rep[BigDecimal] = column[BigDecimal]("quantity")
  def portfolio: ForeignKeyQuery[PortfolioTable, Portfolio] =
    foreignKey("portfolio_fk", portfolioId, portfolios)(_.id)
  def asset: ForeignKeyQuery[AssetTable, Asset] =
    foreignKey("asset_fk", assetId, assets)(_.id)
  primaryKey("id", (portfolioId, assetId))

  def * : ProvenShape[PortfolioAsset] =
    (
      portfolioId,
      assetId,
      quantity,
      createdAt,
      updatedAt
    ) <> ((PortfolioAsset.apply _).tupled, PortfolioAsset.unapply)
}
