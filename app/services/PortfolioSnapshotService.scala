package services

import com.google.inject.ImplementedBy
import models.Portfolio
import models.PortfolioSnapshot
import org.joda.money.BigMoney
import org.joda.money.CurrencyUnit

import java.time.OffsetDateTime
import scala.concurrent.Future

@ImplementedBy(classOf[PortfolioSnapshotServiceImpl])
trait PortfolioSnapshotService {

  def initialSharePrice: BigMoney = BigMoney.of(CurrencyUnit.USD, 100)

  /**
    * Calculate a point in time snapshot of the portfolio value
    */
  def createPortfolioSnapshot(
      portfolio: Portfolio,
      currentDateTime: OffsetDateTime
  ): Future[Either[PortfolioSnapshotError, PortfolioSnapshot]]

}
