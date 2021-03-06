package persistence.tables

import models.Portfolio
import org.joda.money.BigMoney
import play.api.db.slick.HasDatabaseConfigProvider
import slick.lifted.ProvenShape

trait PortfolioTableDefinition {
  this: HasDatabaseConfigProvider[ApplicationPostgresProfile] =>

  import profile.api._

  class PortfolioTable(tag: Tag)
      extends Table[Portfolio](tag, "portfolios")
      with AutoIncIdColumn
      with BaseTableDefinition {
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

  def portfolios: TableQuery[PortfolioTable] = TableQuery[PortfolioTable]
}
