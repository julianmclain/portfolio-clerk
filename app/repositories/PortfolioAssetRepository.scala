package repositories

import db.ApplicationPostgresProfile
import db.Timestamps
import models.PortfolioAsset
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.lifted.ProvenShape
import ApplicationPostgresProfile.api._

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

  def findAllByPortfolioId(portfolioId: Long): Future[Seq[PortfolioAsset]] =
    db.run(portfolioAssets.filter(_.portfolioId === portfolioId).result)

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
    with Timestamps {
  def portfolioId: Rep[Long] = column[Long]("portfolio_id")
  def assetId: Rep[Long] = column[Long]("asset_id")
  def quantity: Rep[BigDecimal] = column[BigDecimal]("quantity")
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
