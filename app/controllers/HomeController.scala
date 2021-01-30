package controllers

import models.Deposit
import models.Portfolio
import models.User
import org.joda.money.Money

import javax.inject._
import play.api.mvc.BaseController
import play.api.mvc._
import repositories.DepositRepository
import repositories.PortfolioRepository
import repositories.UserRepository

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
    val depositRepo: DepositRepository
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
      val d = for {
        u <- userRepo.create(User(0, "hi@aol.com"))
        p <- portfolioRepo.create(Portfolio(0, u.id))
        d <- depositRepo.create(Deposit(0, Money.parse("USD 23.87"), p.id))
      } yield d
      d.map(println)
      Ok(views.html.index())
    }
}
