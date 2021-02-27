package services

import org.joda.money.{BigMoney, CurrencyUnit, Money}
import play.api.libs.ws.WSClient

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

trait FinancialDataClient {

  def getClosingStockPrice(
      ticker: String,
      date: LocalDate
  ): Future[Either[FinancialDataClientError, BigMoney]]
}

case class FinancialDataClientError(msg: String)

/**
  * https://polygon.io/docs/get_v1_open-close__stocksTicker___date__anchor
  */
class PolygonClient @Inject() (
    wsClient: WSClient
)(implicit ec: ExecutionContext)
    extends FinancialDataClient {

  def getClosingStockPrice(
      ticker: String,
      date: LocalDate
  ): Future[Either[FinancialDataClientError, BigMoney]] =
    Future.successful(Right(BigMoney.of(CurrencyUnit.USD, 0)))
}
