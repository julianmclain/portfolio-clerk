package persistence.repositories

import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import models.Portfolio
import models.PortfolioSnapshot
import play.api.db.slick.HasDatabaseConfigProvider
import persistence.tables.ApplicationPostgresProfile
import persistence.tables.PortfolioSnapshotTableDefinition

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
class PortfolioSnapshotRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends HasDatabaseConfigProvider[ApplicationPostgresProfile]
    with PortfolioSnapshotTableDefinition {

  import profile.api._

  def findById(id: Long): Future[Option[PortfolioSnapshot]] =
    db.run(portfolioSnapshots.filter(_.id === id).result.headOption)

  // TODO - not tested
  def getLastSnapshot(portfolio: Portfolio): Future[Option[PortfolioSnapshot]] =
    db.run(
      portfolioSnapshots
        .filter(_.portfolioId === portfolio.id)
        .sortBy(_.snapshotDatetime)
        .result
        .headOption
    )

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
