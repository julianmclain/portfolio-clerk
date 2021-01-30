package repositories

import models.Deposit
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import javax.inject.Inject
import javax.inject.Singleton
import scala.concurrent.ExecutionContext

@Singleton
class DepositRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(
    implicit ec: ExecutionContext
) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class DepositTable(tag: Tag) extends Table[Deposit](tag, "deposits") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def amount: Rep[Long] = column[Long]("amount")
    def portfolioId: Rep[Long] = column[Long]("portfolio_id") // add foreign key

    def * : ProvenShape[Deposit] = (id, amount, portfolioId).mapTo[Deposit]
  }
}
