package repositories

import play.api.db.slick.DatabaseConfigProvider
import javax.inject.Inject
import javax.inject.Singleton
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape
import models.Portfolio

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
class PortfolioRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(
    implicit ec: ExecutionContext
) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class PortfolioTable(tag: Tag)
      extends Table[Portfolio](tag, "portfolios") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def userId: Rep[Long] = column[Long]("user_id") // add foreign key

    def * : ProvenShape[Portfolio] = (id, userId).mapTo[Portfolio]
  }
}
