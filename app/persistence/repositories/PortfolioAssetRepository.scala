package persistence.repositories

import models.AssetSymbol
import models.PortfolioAsset
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import persistence.tables.ApplicationPostgresProfile
import persistence.tables.PortfolioAssetTableDefinition

import javax.inject.Inject
import javax.inject.Singleton
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
class PortfolioAssetRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends HasDatabaseConfigProvider[ApplicationPostgresProfile]
    with PortfolioAssetTableDefinition {
  import profile.api._

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
