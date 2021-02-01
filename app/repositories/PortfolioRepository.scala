package repositories

import models.MoneyWrapper
import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape
import models.Portfolio

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
final class PortfolioRepository @Inject() (
    dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  import CustomColumnTypes.moneyMapper

  private class PortfolioTable(tag: Tag)
      extends Table[Portfolio](tag, "portfolios") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def userId: Rep[Long] = column[Long]("user_id")
    def shareCount: Rep[BigDecimal] = column[BigDecimal]("share_count")
    def sharePrice: Rep[MoneyWrapper] = column[MoneyWrapper]("share_price")
    def totalValue: Rep[MoneyWrapper] = column[MoneyWrapper]("total_value")

    def * : ProvenShape[Portfolio] =
      (
        id,
        userId,
        shareCount,
        sharePrice,
        totalValue
      ) <> ((Portfolio.apply _).tupled, Portfolio.unapply)
  }

  private val portfolios = TableQuery[PortfolioTable]

  def findById(id: Long): Future[Option[Portfolio]] =
    db.run(portfolios.filter(_.id === id).result.headOption)

  def create(portfolio: Portfolio): Future[Portfolio] = {
    val insertQuery = portfolios returning portfolios.map(_.id) into ((_, id) =>
      portfolio.copy(id = id)
    )
    val action = insertQuery += portfolio
    db.run(action)
  }
}
