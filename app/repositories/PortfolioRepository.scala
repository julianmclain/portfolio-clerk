package repositories

import db.ApplicationPostgresProfile
import db.AutoIncId
import db.Timestamps
import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import slick.lifted.ProvenShape
import models.Portfolio
import play.api.db.slick.HasDatabaseConfigProvider

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
class PortfolioRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends HasDatabaseConfigProvider[ApplicationPostgresProfile] {
  import ApplicationPostgresProfile.api._

  class PortfolioTable(tag: Tag)
      extends Table[Portfolio](tag, "portfolios")
      with AutoIncId
      with Timestamps {
    def userId: Rep[Long] = column[Long]("user_id")
    def name: Rep[String] = column[String]("name")

    def * : ProvenShape[Portfolio] =
      (
        id,
        userId,
        name,
        createdAt,
        updatedAt
      ) <> ((Portfolio.apply _).tupled, Portfolio.unapply)
  }

  private val portfolios = TableQuery[PortfolioTable]

  def findById(id: Long): Future[Option[Portfolio]] =
    db.run(portfolios.filter(_.id === id).result.headOption)

  def create(portfolio: Portfolio): Future[Portfolio] = {
    val insertQuery =
      portfolios returning portfolios.map(_.id) into ((record, id) =>
        portfolio.copy(
          id = id,
          updatedAt = record.updatedAt,
          createdAt = record.createdAt
        )
      )
    val action = insertQuery += portfolio
    db.run(action)
  }
}
