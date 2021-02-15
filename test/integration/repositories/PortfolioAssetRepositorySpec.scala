package integration.repositories

import models.PortfolioAsset
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.Mode
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.Injector
import play.api.inject.guice.GuiceApplicationBuilder
import repositories.PortfolioAssetRepository

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class PortfolioAssetRepositorySpec extends PlaySpec with GuiceOneAppPerSuite {

  implicit override lazy val app = new GuiceApplicationBuilder()
    .in(Mode.Test)
    .build
  lazy val injector: Injector = app.injector
  lazy val dbConfProvider: DatabaseConfigProvider =
    injector.instanceOf[DatabaseConfigProvider]

  def portfolioAssetRepo(implicit app: Application): PortfolioAssetRepository =
    Application.instanceCache[PortfolioAssetRepository].apply(app)

  "PortfolioAssetRepository" should {
    "create PortfolioAsset" in {
      val portfolioAsset = Await.result(
        portfolioAssetRepo.create(PortfolioAsset(10, 1, 1, 1, None, None)),
        Duration.Inf
      )
      println(portfolioAsset)
      portfolioAsset.assetId mustBe 10
    }
  }

}
