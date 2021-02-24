package repositories

import db.ApplicationPostgresProfile
import db.ApplicationPostgresProfile.api._
import db.Timestamps
import models.PortfolioAsset
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
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

  val portfolioAssets = TableQuery[PortfolioAssetTable]

  // TODO - not tested
  def findAllByPortfolioId(portfolioId: Long): Future[Seq[PortfolioAsset]] =
    db.run(portfolioAssets.filter(_.portfolioId === portfolioId).result)

  def create(portfolioAsset: PortfolioAsset): Future[PortfolioAsset] = {
    val insertQuery =
      portfolioAssets returning portfolioAssets.map(_.id) into ((record, id) =>
        portfolioAsset.copy(
          id = id,
          updatedAt = record.updatedAt,
          createdAt = record.createdAt
        )
      )
    val action = insertQuery += portfolioAsset
    db.run(action)
  }
}

class PortfolioAssetTable(tag: Tag)
  extends Table[PortfolioAsset](tag, "portfolio_assets")
    with Timestamps {
  def id: Rep[Long] = column[Long]("id")
  def portfolioId: Rep[Long] = column[Long]("portfolio_id")
  def assetId: Rep[Long] = column[Long]("asset_id")
  def quantity: Rep[BigDecimal] = column[BigDecimal]("quantity")
  primaryKey("id", (portfolioId, assetId))
  def assset = foreignKey("asset_fk", assetId, AssetRepository.assets)
//  def portfolio = foreignKey()
  def * : ProvenShape[PortfolioAsset] =
    (
      id,
      portfolioId,
      assetId,
      quantity,
      createdAt,
      updatedAt
    ) <> ((PortfolioAsset.apply _).tupled, PortfolioAsset.unapply)
}
