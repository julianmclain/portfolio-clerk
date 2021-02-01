package repositories

import models.MoneyWrapper
import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape
import models.PortfolioSnapshot

import java.time.LocalDate
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
  import CustomColumnTypes.moneyMapper

  private class PortfolioSnapshotTable(tag: Tag)
      extends Table[PortfolioSnapshot](tag, "portfolio_snapshots") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def portfolioId: Rep[Long] = column[Long]("portfolio_id")
    def openingShareCount: Rep[BigDecimal] =
      column[BigDecimal]("opening_share_count")
    def openingSharePrice: Rep[MoneyWrapper] =
      column[MoneyWrapper]("opening_share_price")
    def openingValue: Rep[MoneyWrapper] = column[MoneyWrapper]("opening_value")
    def netCashFlow: Rep[MoneyWrapper] = column[MoneyWrapper]("net_cash_flow")
    def cashFlowSharePrice: Rep[MoneyWrapper] =
      column[MoneyWrapper]("cash_flow_share_price")
    def numShareChange: Rep[BigDecimal] = column[BigDecimal]("num_share_change")
    def closingShareCount: Rep[BigDecimal] =
      column[BigDecimal]("closing_share_count")
    def closingSharePrice: Rep[MoneyWrapper] =
      column[MoneyWrapper]("closing_share_price")
    def closingValue: Rep[MoneyWrapper] = column[MoneyWrapper]("closing_value")
    def netReturn: Rep[BigDecimal] = column[BigDecimal]("net_return")
    def date: Rep[LocalDate] = column[LocalDate]("date")

    def * : ProvenShape[PortfolioSnapshot] =
      (
        id,
        portfolioId,
        openingShareCount,
        openingSharePrice,
        openingValue,
        netCashFlow,
        cashFlowSharePrice,
        numShareChange,
        closingShareCount,
        closingSharePrice,
        closingValue,
        netReturn,
        date
      ) <> (
        (PortfolioSnapshot.apply _).tupled,
        PortfolioSnapshot.unapply
      )
  }

  private val portfolioSnapshots = TableQuery[PortfolioSnapshotTable]

  def findById(id: Long): Future[Option[PortfolioSnapshot]] =
    db.run(portfolioSnapshots.filter(_.id === id).result.headOption)

  def create(
      portfolioSnapshot: PortfolioSnapshot
  ): Future[PortfolioSnapshot] = {
    val insertQuery =
      portfolioSnapshots returning portfolioSnapshots.map(_.id) into ((_, id) =>
        portfolioSnapshot.copy(id = id)
      )
    val action = insertQuery += portfolioSnapshot
    db.run(action)
  }
}
