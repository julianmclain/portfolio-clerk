package repositories

import models.PortfolioAsset
import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
final class PortfolioAssetRepository @Inject() (
    dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  import CustomColumnTypes.moneyMapper

  private class PortfolioAssetTable(tag: Tag)
      extends Table[PortfolioAsset](tag, "portfolio_assets") {
    def id = primaryKey("id", (portfolioId, assetId))
    def portfolioId: Rep[Long] = column[Long]("portfolio_id")
    def assetId: Rep[Long] = column[Long]("asset_id")
    def quantity: Rep[BigDecimal] = column[BigDecimal]("quantity")
    def * : ProvenShape[PortfolioAsset] =
      (
        portfolioId,
        assetId,
        quantity
      ) <> ((PortfolioAsset.apply _).tupled, PortfolioAsset.unapply)
  }

  private val portfolioAssets = TableQuery[PortfolioAssetTable]

  def findAllByPortfolioId(portfolioId: Long): Future[Seq[PortfolioAsset]] =
    db.run(portfolioAssets.filter(_.portfolioId === portfolioId).result)

  def create(portfolioAsset: PortfolioAsset): Future[PortfolioAsset] = {
    val insertQuery =
      portfolioAssets returning portfolioAssets into ((pa, id) =>
        PortfolioAsset(pa.portfolioId, pa.assetId, pa.quantity)
      )
    val action = insertQuery += portfolioAsset
    db.run(action)
  }
}
