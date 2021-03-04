package repositories

import db.ApplicationPostgresProfile
import db.ApplicationPostgresProfile.api._
import db.AutoIncIdColumn
import db.TimestampColumns
import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import slick.lifted.ProvenShape
import models.Portfolio
import org.joda.money.BigMoney
import play.api.db.slick.HasDatabaseConfigProvider

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
class PortfolioRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends HasDatabaseConfigProvider[ApplicationPostgresProfile] {

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

private[repositories] class PortfolioTable(tag: Tag)
    extends Table[Portfolio](tag, "portfolios")
    with AutoIncIdColumn
    with TimestampColumns {
  def userId: Rep[Long] = column[Long]("user_id")
  def cashBalance: Rep[BigMoney] = column[BigMoney]("cash_balance")
  def name: Rep[String] = column[String]("name")

  def * : ProvenShape[Portfolio] =
    (
      id,
      userId,
      name,
      cashBalance,
      createdAt,
      updatedAt
    ) <> ((Portfolio.apply _).tupled, Portfolio.unapply)
}
