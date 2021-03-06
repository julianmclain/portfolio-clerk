package persistence.tables

import models.Asset
import models.Portfolio
import models.PortfolioAsset
import persistence.ApplicationPostgresProfile
import play.api.db.slick.HasDatabaseConfigProvider
import slick.lifted.ForeignKeyQuery
import slick.lifted.ProvenShape

trait PortfolioAssetTableDefinition
    extends PortfolioTableDefinition
    with AssetTableDefinition {
  this: HasDatabaseConfigProvider[ApplicationPostgresProfile] =>

  import profile.api._

  class PortfolioAssetTable(tag: Tag)
      extends Table[PortfolioAsset](tag, "portfolio_assets")
      with TimestampColumns {

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

  def portfolioAssets: TableQuery[PortfolioAssetTable] =
    TableQuery[PortfolioAssetTable]
}
