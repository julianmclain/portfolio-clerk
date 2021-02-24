package services

import models.Portfolio
import models.PortfolioAsset
import models.PortfolioSnapshot
import org.joda.money.{BigMoney, CurrencyUnit, Money}
import repositories.{AssetRepository, PortfolioAssetRepository, PortfolioRepository, PortfolioSnapshotRepository}
import util.GlobalConstants.{GLOBAL_ROUNDING_MODE, GLOBAL_TZ_OFFSET}

import java.time.{OffsetDateTime, ZoneId, ZoneOffset}
import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.math.BigDecimal.javaBigDecimal2bigDecimal

trait PortfolioSnapshotService {

  /**
    * Calculate a point in time snapshot of the portfolio value
    */
  def createPortfolioSnapshot(
      portfolio: Portfolio
  ): Future[PortfolioSnapshot]

}

class PortfolioSnapshotServiceImpl @Inject() (
                                             assetRepo: AssetRepository,
                                             polygonClient: PolygonClient,
    portfolioRepo: PortfolioRepository,
    portfolioSnapshotRepo: PortfolioSnapshotRepository,
    portfolioAssetRepo: PortfolioAssetRepository
)(implicit ec: ExecutionContext)
    extends PortfolioService {

  case class SnapshotError(message: String)
  private val initialShareCount = BigDecimal(1000)
  private val initialSnapshotDatetime = OffsetDateTime.now(GLOBAL_TZ_OFFSET)
  private val initialSharePrice = BigMoney.of(CurrencyUnit.USD, 10)
  /**
    * v1 assumes that this method will be called 1 time per day once after-hours trading closes.
    *
    * To calculate a portfolio snapshot:
    *  1. fetch all portfolio assets
    *  2. for each portfolio asset, calculate the current value
    *  3. fetch all deposit / withdrawals since the last snapshot
    *  4. run the calculation
    *     - opening values taken from portfolio record last snapshot value
    *     - netCashFlow and numShareChange comes from any deposits / withdrawals
    *     - closingShareCount = openingShareCount + numSharesChange
    *     - closingValue = openingValue + change in value for every asset in the portfolio
    *     - closingSharePrice = closingValue / shareCount
    *     - netReturn = (closingSharePrice - openingSharePrice) / 100
    */
  def createPortfolioSnapshot(
      portfolio: Portfolio,
      currentDatetime: OffsetDateTime
  ): Future[PortfolioSnapshot] =
    portfolioSnapshotRepo.getLastSnapshot(portfolio).flatMap {
      case Some(prev) => createIncrementalSnapshot(portfolio, prev, currentDatetime)
      case None => createFirstSnapshot()
    }

  private def calculateNetCashFlow(
      portfolio: Portfolio,
      since: OffsetDateTime
  ): (Money, BigDecimal) =
    ???

  private def calculateNetAssetValue(
      portfolioAssets: Seq[PortfolioAsset]
  ): Future[Either[SnapshotError, Money]] = {
    val rv = portfolioAssets.map { portfolioAsset =>
      assetRepo.findById(portfolioAsset.assetId).flatMap {
        case Some(asset) => Future.successful(Right(Money.of(CurrencyUnit.USD, 0)))
        case None => Future.successful(Left(SnapshotError("No asset")))
      }
  }
    Future.successful(Right(Money.of(CurrencyUnit.USD, 0)))
  }
  //    } yield Money.of(CurrencyUnit.USD, 10)).foldLeft(Money.of(CurrencyUnit.USD, 0))((sum, next) => sum.plus(next))
  private def createFirstSnapshot(): Future[PortfolioSnapshot] = ???

  private def createIncrementalSnapshot(portfolio: Portfolio, prevSnapshot: PortfolioSnapshot, currentDatetime: OffsetDateTime): Future[PortfolioSnapshot] =
    for {
      portfolioAssets <- portfolioAssetRepo.findAllByPortfolioId(portfolio.id)
      netAssetValue <- calculateNetAssetValue(portfolioAssets)
      (netCashFlow, numShareChange) = calculateNetCashFlow(portfolio, prevSnapshot.snapshotDatetime)
      openingSharePrice = prevSnapshot.closingSharePrice
      currentShareCount = prevSnapshot.closingShareCount + numShareChange
      closingSharePrice = netAssetValue.dividedBy(currentShareCount.bigDecimal, GLOBAL_ROUNDING_MODE)
    } yield {
      portfolioSnapshotRepo.create(
        PortfolioSnapshot(
          id = 0,
          portfolioId = portfolio.id,
          openingShareCount = prevSnapshot.closingShareCount,
          openingSharePrice = openingSharePrice,
          openingValue = prevSnapshot.closingValue,
          netCashFlow = netCashFlow,
          numShareChange = numShareChange,
          closingShareCount = currentShareCount,
          closingSharePrice = closingSharePrice,
          closingValue = netAssetValue,
          netReturn =
            closingSharePrice.minus(openingSharePrice).getAmount / 100,
          snapshotDatetime = currentDatetime,
          None,
          None
        )
      )
    }
}
