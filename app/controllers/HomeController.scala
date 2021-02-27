package controllers

import models.Asset
import models.Deposit
import models.Portfolio
import models.PortfolioAsset
import models.PortfolioSnapshot
import models.Stock
import models.User
import org.joda.money.BigMoney
import org.joda.money.Money

import javax.inject._
import play.api.mvc.BaseController
import play.api.mvc._
import repositories.AssetRepository
import repositories.DepositRepository
import repositories.PortfolioAssetRepository
import repositories.PortfolioRepository
import repositories.PortfolioSnapshotRepository
import repositories.UserRepository

import java.time.OffsetDateTime
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
    val portfolioSnapshotRepo: PortfolioSnapshotRepository,
    val assetRepo: AssetRepository,
    val portfolioAssetRepo: PortfolioAssetRepository
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
      val test = for {
        u <- userRepo.create(
          User(
            0,
            "hi@aol.com",
            OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
            None,
            None
          )
        )
        p <- portfolioRepo.create(
          Portfolio(0, u.id, "Test portfolio", None, None)
        )
//        d <- depositRepo.create(
//          Deposit(
//            0,
//            p.id,
//            Money.parse("USD 23.87"),
//            OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
//            None,
//            None
//          )
//        )
        d <- depositRepo.test(
          Deposit(
            0,
            p.id,
            BigMoney.parse("USD 9999.87"),
            OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
            None,
            None
          ),
          Asset(
            id = 0,
            portfolioId = p.id,
            assetName = "Test stock",
            assetSymbol = "TST",
            assetType = Stock,
            None,
            None
          )
        )
        a <- assetRepo.create(
          Asset(
            id = 0,
            portfolioId = p.id,
            assetName = "Apple stock",
            assetSymbol = "AAPL",
            assetType = Stock,
            None,
            None
          )
        )
        pa <- portfolioAssetRepo.create(
          PortfolioAsset(
            portfolioId = p.id,
            assetId = a.id,
            quantity = 1,
            None,
            None
          )
        )
      } yield {
        println(pa)
      }

      Ok(views.html.index())
    }
}
