package repositories

import db.ApplicationPostgresProfile
import db.AutoIncId
import db.Timestamps
import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import slick.lifted.ProvenShape
import models.PortfolioSnapshot
import org.joda.money.Money
import play.api.db.slick.HasDatabaseConfigProvider

import java.time.OffsetDateTime
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
class PortfolioSnapshotRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends HasDatabaseConfigProvider[ApplicationPostgresProfile] {

  import ApplicationPostgresProfile.api._

  private class PortfolioSnapshotTable(tag: Tag)
      extends Table[PortfolioSnapshot](tag, "portfolio_snapshots")
      with AutoIncId
      with Timestamps {
    def portfolioId: Rep[Long] = column[Long]("portfolio_id")
    def openingShareCount: Rep[BigDecimal] =
      column[BigDecimal]("opening_share_count")
    def openingSharePrice: Rep[Money] =
      column[Money]("opening_share_price")
    def openingValue: Rep[Money] = column[Money]("opening_value")
    def netCashFlow: Rep[Money] = column[Money]("net_cash_flow")
    def numShareChange: Rep[BigDecimal] = column[BigDecimal]("num_share_change")
    def closingShareCount: Rep[BigDecimal] =
      column[BigDecimal]("closing_share_count")
    def closingSharePrice: Rep[Money] =
      column[Money]("closing_share_price")
    def closingValue: Rep[Money] = column[Money]("closing_value")
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

  private val portfolioSnapshots = TableQuery[PortfolioSnapshotTable]

  def findById(id: Long): Future[Option[PortfolioSnapshot]] =
    db.run(portfolioSnapshots.filter(_.id === id).result.headOption)

  def create(
      portfolioSnapshot: PortfolioSnapshot
  ): Future[PortfolioSnapshot] = {
    val insertQuery =
      portfolioSnapshots returning portfolioSnapshots.map(_.id) into (
        (record, id) =>
          portfolioSnapshot.copy(
            id = id,
            updatedAt = record.updatedAt,
            createdAt = record.createdAt
          )
      )
    val action = insertQuery += portfolioSnapshot
    db.run(action)
  }
}
