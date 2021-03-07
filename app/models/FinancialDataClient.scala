package models

import com.google.inject.ImplementedBy
import org.joda.money.BigMoney

import java.time.LocalDate
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.FiniteDuration

@ImplementedBy(classOf[PolygonClient])
trait FinancialDataClient {

  def timeout: FiniteDuration = 1000.millis

  def getClosingStockPrice(
      assetSymbol: AssetSymbol,
      date: LocalDate
  ): Future[Either[FinancialDataClientError, BigMoney]]
}
