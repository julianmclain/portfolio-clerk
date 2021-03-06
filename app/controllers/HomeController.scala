package controllers

import models.Asset
import models.AssetSymbol
import models.Deposit
import models.Portfolio
import models.PortfolioAsset
import models.PortfolioSnapshot
import models.Stock
import models.User
import org.joda.money.BigMoney

import javax.inject._
import play.api.mvc.BaseController
import play.api.mvc._
import persistence.repositories.AssetRepository
import persistence.repositories.DepositRepository
import persistence.repositories.PortfolioAssetRepository
import persistence.repositories.PortfolioRepository
import persistence.repositories.PortfolioSnapshotRepository
import persistence.repositories.UserRepository
import services.FinancialDataClient
import services.PortfolioSnapshotService

import java.time.LocalDate
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
    val portfolioAssetRepo: PortfolioAssetRepository,
    val portfolioSnapshotService: PortfolioSnapshotService,
    val fd: FinancialDataClient
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
          Portfolio(
            0,
            u.id,
            "Test portfolio",
            BigMoney.parse("USD 1000"),
            None,
            None
          )
        )
        p2 <- portfolioRepo.findById(1L)
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
        a <- assetRepo.create(
          Asset(
            id = 0,
            name = "Apple stock",
            assetSymbol = AssetSymbol("AAPL"),
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
        stuff <- portfolioAssetRepo.findAssetQuantities(1L)
        snapshot <- portfolioSnapshotService.createPortfolioSnapshot(
          p2.get,
          OffsetDateTime.parse("2021-02-25T11:03:18.770996-08:00")
        )
      } yield {
        println(snapshot)
      }
      Ok(views.html.index())
    }
}
