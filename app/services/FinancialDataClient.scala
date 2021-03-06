package services

import com.google.inject.ImplementedBy
import models.AssetSymbol
import org.joda.money.BigMoney

import java.time.LocalDate
import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.Future

@ImplementedBy(classOf[PolygonClient])
trait FinancialDataClient {

  def timeout: FiniteDuration = 1000.millis

  def getClosingStockPrice(
      assetSymbol: AssetSymbol,
      date: LocalDate
  ): Future[Either[FinancialDataClientError, BigMoney]]
}
