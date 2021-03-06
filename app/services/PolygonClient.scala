package services

import models.AssetSymbol
import org.joda.money.BigMoney
import org.joda.money.CurrencyUnit
import play.api.Configuration
import play.api.libs.json.Json
import play.api.libs.json.OFormat
import play.api.libs.ws.WSClient

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

class PolygonClient @Inject() (
    wsClient: WSClient,
    config: Configuration
)(implicit ec: ExecutionContext)
    extends FinancialDataClient {

  case class StockOpenCloseResponse(symbol: String, close: Double)

  implicit val stockOpenCloseResponseFmt: OFormat[StockOpenCloseResponse] =
    Json.format[StockOpenCloseResponse]

  private val apiKey = config.get[String]("polygonApiKey")

  /**
    * Get the closing price for a stock
    *
    * See docs: [[https://polygon.io/docs/get_v1_open-close__stocksTicker___date__anchor]]
    *
    * @param assetSymbol ticker symbol for the stock
    * @param date        the date
    * @return an error or the closing price for the date
    */
  def getClosingStockPrice(
      assetSymbol: AssetSymbol,
      date: LocalDate
  ): Future[Either[FinancialDataClientError, BigMoney]] = {
    val responseFt = wsClient
      .url(s"https://api.polygon.io/v1/open-close/${assetSymbol.value}/$date")
      .addQueryStringParameters(
        "apiKey" -> apiKey,
        "unadjusted" -> "false"
      )
      .withRequestTimeout(10000.millis)
      .get()

    responseFt.map { resp =>
      val closingPrice = resp.json.as[StockOpenCloseResponse].close
      Right(BigMoney.of(CurrencyUnit.USD, closingPrice))
    }
    // TODO - add error handling logic
  }
}
