package repositories

import db.ApplicationPostgresProfile
import db.ApplicationPostgresProfile.api._
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

<<<<<<< HEAD
  val portfolioAssets = TableQuery[PortfolioAssetTable]
=======
  private val portfolioAssets = TableQuery[PortfolioAssetTable]
>>>>>>> c2dc6214f65cd823687509af093e9a4f0799d136

  // TODO - not tested
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

<<<<<<< HEAD
class PortfolioAssetTable(tag: Tag)
  extends Table[PortfolioAsset](tag, "portfolio_assets")
    with Timestamps {
  def id: Rep[Long] = column[Long]("id")
=======
private[repositories] class PortfolioAssetTable(tag: Tag)
    extends Table[PortfolioAsset](tag, "portfolio_assets")
    with Timestamps {
>>>>>>> c2dc6214f65cd823687509af093e9a4f0799d136
  def portfolioId: Rep[Long] = column[Long]("portfolio_id")
  def assetId: Rep[Long] = column[Long]("asset_id")
  def quantity: Rep[BigDecimal] = column[BigDecimal]("quantity")
  primaryKey("id", (portfolioId, assetId))
<<<<<<< HEAD
  def assset = foreignKey("asset_fk", assetId, AssetRepository.assets)
//  def portfolio = foreignKey()
  def * : ProvenShape[PortfolioAsset] =
    (
      id,
=======
  def * : ProvenShape[PortfolioAsset] =
    (
>>>>>>> c2dc6214f65cd823687509af093e9a4f0799d136
      portfolioId,
      assetId,
      quantity,
      createdAt,
      updatedAt
    ) <> ((PortfolioAsset.apply _).tupled, PortfolioAsset.unapply)
}
