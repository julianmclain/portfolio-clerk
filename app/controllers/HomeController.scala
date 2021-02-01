package controllers

import models.Asset
import models.Deposit
import models.MoneyWrapper
import models.Portfolio
import models.PortfolioSnapshot
import models.Stock
import models.User

import javax.inject._
import play.api.mvc.BaseController
import play.api.mvc._
import repositories.AssetRepository
import repositories.DepositRepository
import repositories.PortfolioRepository
import repositories.PortfolioSnapshotRepository
import repositories.UserRepository

import java.time.LocalDate
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

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
    val assetRepo: AssetRepository
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
        u <- userRepo.create(User(0, "hi@aol.com"))
        p <- portfolioRepo.create(
          Portfolio(
            id = 0,
            userId = u.id,
            shareCount = BigDecimal(1000),
            sharePrice = MoneyWrapper("USD 50"),
            totalValue = MoneyWrapper("USD 50000")
          )
        )
        d <- depositRepo.create(Deposit(0, MoneyWrapper("USD 23.87"), p.id))
        a <- assetRepo.create(
          Asset(
            id = 0,
            portfolioId = p.id,
            assetName = "Apple stock",
            assetSymbol = "AAPL",
            assetType = Stock,
            quantity = 1,
            unitPrice = MoneyWrapper("USD 20"),
            totalValue = MoneyWrapper("USD 20")
          )
        )
      } yield {
        // To create a portfolio snapshot:
        // 1. fetch the portfolio record
        // 2. fetch all deposit records for that day
        // 3. fetch all asset records with quantity > 0 (what about cash...)
        // run the calculation
        // - opening values taken from portfolio record
        // - netCashFlow, cashFlowSharePrice, and numShareChange comes from any deposits / withdraws
        // - closingShareCount = openingShareCount + numSharesChange
        // - closingValue = openingValue + change in value for every asset in the portfolio
        // - closingSharePrice = closingValue / shareCount
        // - netReturn = (closingSharePrice - openingSharePrice) / 100
        PortfolioSnapshot(
          id = 0,
          portfolioId = p.id,
          openingShareCount = p.shareCount,
          openingSharePrice = p.sharePrice,
          openingValue = p.totalValue,
          netCashFlow = MoneyWrapper("USD 0"),
          cashFlowSharePrice = MoneyWrapper("USD 0"),
          numShareChange = BigDecimal(0),
          closingShareCount = p.shareCount,
          closingSharePrice = MoneyWrapper("USD 23.87"),
          closingValue = MoneyWrapper("USD 23.87"),
          netReturn = BigDecimal(1),
          date = LocalDate.parse("2007-12-03")
        )
      }

      Ok(views.html.index())
    }
}
