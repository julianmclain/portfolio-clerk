package persistence.tables

import models.PortfolioSnapshot
import org.joda.money.BigMoney
import play.api.db.slick.HasDatabaseConfigProvider
import slick.lifted.ProvenShape

import java.time.OffsetDateTime

trait PortfolioSnapshotTableDefinition {
  this: HasDatabaseConfigProvider[ApplicationPostgresProfile] =>

  import profile.api._

  class PortfolioSnapshotTable(tag: Tag)
      extends Table[PortfolioSnapshot](tag, "portfolio_snapshots")
      with AutoIncIdColumn
      with BaseTableDefinition {
    def portfolioId: Rep[Long] = column[Long]("portfolio_id")
    def openingShareCount: Rep[BigDecimal] =
      column[BigDecimal]("opening_share_count")
    def openingSharePrice: Rep[BigMoney] =
      column[BigMoney]("opening_share_price")
    def openingValue: Rep[BigMoney] = column[BigMoney]("opening_value")
    def netCashFlow: Rep[BigMoney] = column[BigMoney]("net_cash_flow")
    def numShareChange: Rep[BigDecimal] = column[BigDecimal]("num_share_change")
    def closingShareCount: Rep[BigDecimal] =
      column[BigDecimal]("closing_share_count")
    def closingSharePrice: Rep[BigMoney] =
      column[BigMoney]("closing_share_price")
    def closingValue: Rep[BigMoney] = column[BigMoney]("closing_value")
    def netReturn: Rep[BigDecimal] = column[BigDecimal]("net_return")
    def snapshotDatetime: Rep[OffsetDateTime] =
      column[OffsetDateTime]("snapshot_datetime")

    def * : ProvenShape[PortfolioSnapshot] =
      (
        id,
        portfolioId,
        openingShareCount,
        openingSharePrice,
        openingValue,
        netCashFlow,
        numShareChange,
        closingShareCount,
        closingSharePrice,
        closingValue,
        netReturn,
        snapshotDatetime,
        createdAt,
        updatedAt
      ) <> (
        (PortfolioSnapshot.apply _).tupled,
        PortfolioSnapshot.unapply
      )
  }

  def portfolioSnapshots = TableQuery[PortfolioSnapshotTable]
}
