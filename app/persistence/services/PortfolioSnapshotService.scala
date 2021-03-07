package persistence.services

import com.google.inject.ImplementedBy
import models.Portfolio
import models.PortfolioSnapshot
import models.PortfolioSnapshotError
import org.joda.money.BigMoney
import org.joda.money.CurrencyUnit

import java.time.OffsetDateTime
import scala.concurrent.Future

/**
  * Service for interacting with a [[PortfolioSnapshot]]
  */
@ImplementedBy(classOf[PostgresPortfolioSnapshotService])
trait PortfolioSnapshotService {

  def initialSharePrice: BigMoney = BigMoney.of(CurrencyUnit.USD, 100)

  /**
    * Create a point in time snapshot of the [[Portfolio]] value
    *
    * @param portfolio the portfolio
    * @param currentDateTime the current date time
    * @return a portfolio snapshot
    */
  def create(
      portfolio: Portfolio,
      currentDateTime: OffsetDateTime
  ): Future[Either[PortfolioSnapshotError, PortfolioSnapshot]]
}
