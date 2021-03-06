package integration.services

import models.AssetSymbol
import org.joda.money.BigMoney
import org.mockito.IdiomaticMockito
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Helpers.baseApplicationBuilder.injector
import play.api.test.Injecting
import services.PolygonClient

import java.time.LocalDate
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

class PolygonClientSpec
    extends PlaySpec
    with GuiceOneAppPerTest
    with Injecting
    with IdiomaticMockito {
  "PolygonClient" should {
    "successfully fetch stock price at close" in {
      implicit lazy val ec: ExecutionContext =
        injector.instanceOf[ExecutionContext]
      val polygonClient = injector.instanceOf[PolygonClient]
      val result = Await.result(
        polygonClient.getClosingStockPrice(
          AssetSymbol("SNOW"),
          LocalDate.parse("2021-02-25")
        ),
        Duration.Inf
      )
      result mustBe Right(BigMoney.parse("USD 260.34"))
    }
  }
}
