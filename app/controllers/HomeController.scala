package controllers

import models.Deposit
import models.MoneyWrapper
import models.Portfolio
import models.PortfolioSnapshot
import models.User

import javax.inject._
import play.api.mvc.BaseController
import play.api.mvc._
import repositories.DepositRepository
import repositories.PortfolioRepository
import repositories.PortfolioSnapshotRepository
import repositories.UserRepository

import java.time.LocalDate
import scala.concurrent.ExecutionContext

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() (
    val controllerComponents: ControllerComponents,
    val userRepo: UserRepository,
    val portfolioRepo: PortfolioRepository,
    val depositRepo: DepositRepository,
    val portfolioSnapshotRepo: PortfolioSnapshotRepository
)(implicit ec: ExecutionContext)
    extends BaseController {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index() =
    Action { implicit request: Request[AnyContent] =>
      val ps = for {
        u <- userRepo.create(User(0, "hi@aol.com"))
        p <- portfolioRepo.create(Portfolio(0, u.id))
        d <- depositRepo.create(Deposit(0, MoneyWrapper("USD 23.87"), p.id))
        ps <- portfolioSnapshotRepo.create(
          PortfolioSnapshot(
            id = 0,
            portfolioId = p.id,
            openingShareCount = BigDecimal(1),
            openingSharePrice = MoneyWrapper("USD 23.87"),
            openingValue = MoneyWrapper("USD 23.87"),
            netCashFlow = MoneyWrapper("USD 23.87"),
            cashFlowSharePrice = MoneyWrapper("USD 23.87"),
            numShareChange = BigDecimal(1),
            closingShareCount = BigDecimal(1),
            closingSharePrice = MoneyWrapper("USD 23.87"),
            closingValue = MoneyWrapper("USD 23.87"),
            netReturn = BigDecimal(1),
            date = LocalDate.parse("2007-12-03")
          )
        )
      } yield ps
      ps.map(println)
      Ok(views.html.index())
    }
}
