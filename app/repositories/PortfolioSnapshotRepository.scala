package repositories

import play.api.db.slick.DatabaseConfigProvider
import javax.inject.Inject
import javax.inject.Singleton
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape
import models.PortfolioSnapshot

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
final class PortfolioSnapshotRepository @Inject() (
    dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class PortfolioSnapshotTable(tag: Tag)
      extends Table[PortfolioSnapshot](tag, "portfolio_snapshots") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def openingShareCount: Rep[Long] = column[Long]("opening_share_count")
    def openingSharePrice: Rep[Long] = column[Long]("opening_share_price")
    def openingValue: Rep[Long] = column[Long]("opening_value")
    def netCashFlows: Rep[Long] = column[Long]("net_cash_flows")
    def cashFlowSharePrice: Rep[Long] = column[Long]("cash_flow_share_price")
    def numShareChange: Rep[Long] = column[Long]("num_share_change")
    def closingShareCount: Rep[Long] = column[Long]("closing_share_count")
    def closingSharePrice: Rep[Long] = column[Long]("closing_share_price")
    def closingValue: Rep[Long] = column[Long]("closing_value")
    def netReturnPercentage: Rep[Long] = column[Long]("net_return_percentage")

    def * : ProvenShape[PortfolioSnapshot] =
      (
        id,
        openingShareCount,
        openingSharePrice,
        openingValue,
        netCashFlows,
        cashFlowSharePrice,
        numShareChange,
        closingShareCount,
        closingSharePrice,
        closingValue,
        netReturnPercentage
      ) <> (
        (PortfolioSnapshot.apply _).tupled,
        PortfolioSnapshot.unapply
      )
  }
}
