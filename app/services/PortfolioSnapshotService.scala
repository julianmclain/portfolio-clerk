package services

import models.Portfolio
import models.PortfolioAsset
import models.PortfolioSnapshot
import org.joda.money.BigMoney
import org.joda.money.CurrencyUnit
import repositories.AssetRepository
import repositories.DepositRepository
import repositories.PortfolioAssetRepository
import repositories.PortfolioSnapshotRepository
import repositories.WithdrawalRepository
import util.GlobalConstants.GLOBAL_ROUNDING_MODE

import java.time.OffsetDateTime
import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.math.BigDecimal.javaBigDecimal2bigDecimal

trait PortfolioSnapshotService {

  /**
    * Calculate a point in time snapshot of the portfolio value
    */
  def createPortfolioSnapshot(
      portfolio: Portfolio,
      currentDateTime: OffsetDateTime
  ): Future[Either[PortfolioSnapshotError, PortfolioSnapshot]]

}

case class PortfolioSnapshotError(message: String)

class PortfolioSnapshotServiceImpl @Inject() (
    assetRepo: AssetRepository,
    depositRepo: DepositRepository,
    withdrawalRepo: WithdrawalRepository,
    fdClient: FinancialDataClient,
    portfolioSnapshotRepo: PortfolioSnapshotRepository,
    portfolioAssetRepo: PortfolioAssetRepository
)(implicit ec: ExecutionContext)
    extends PortfolioSnapshotService {

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
  ): Future[Either[PortfolioSnapshotError, PortfolioSnapshot]] =
    portfolioSnapshotRepo.getLastSnapshot(portfolio).flatMap {
      case Some(prev) =>
        createIncrementalSnapshot(portfolio, prev, currentDatetime)
      case None => createFirstSnapshot(portfolio, currentDatetime)
    }

  /**
    * If no snapshot taken before
    * - Get all deposit and withdrawals
    * -
    * @param portfolio
    * @param currentDatetime
    * @return
    */
  private def createFirstSnapshot(
      portfolio: Portfolio,
      currentDatetime: OffsetDateTime
  ): Future[Either[PortfolioSnapshotError, PortfolioSnapshot]] = {
//    for {
//      deposits <- depo
//    }
    Future.successful(Left(PortfolioSnapshotError("stuff")))
  }

  private def createIncrementalSnapshot(
      portfolio: Portfolio,
      prevSnapshot: PortfolioSnapshot,
      currentDatetime: OffsetDateTime
  ): Future[Either[PortfolioSnapshotError, PortfolioSnapshot]] = {
    val netAssetValueOrErrorFt = for {
      portfolioAssets <- portfolioAssetRepo.findAllByPortfolioId(portfolio.id)
      navOrError <- calculateNetAssetValue(portfolioAssets)
    } yield navOrError

    val (netCashFlow, numShareChange) =
      calculateNetCashFlow(portfolio, prevSnapshot.snapshotDatetime)
    val openingSharePrice = prevSnapshot.closingSharePrice
    val currentShareCount = prevSnapshot.closingShareCount + numShareChange

    netAssetValueOrErrorFt.flatMap {
      case Right(nav) => {
        val closingSharePrice = nav.dividedBy(
          currentShareCount.bigDecimal,
          GLOBAL_ROUNDING_MODE
        )
        portfolioSnapshotRepo
          .create(
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
              closingValue = nav,
              netReturn =
                closingSharePrice.minus(openingSharePrice).getAmount / 100,
              snapshotDatetime = currentDatetime,
              None,
              None
            )
          )
          .map(Right(_))
      }
      case Left(error) => Future.successful(Left(error))
    }
  }

  private def calculateNetCashFlow(
      portfolio: Portfolio,
      since: OffsetDateTime
  ): (BigMoney, BigDecimal) =
    ???

  private def calculateNetAssetValue(
      portfolioAssets: Seq[PortfolioAsset]
  ): Future[Either[PortfolioSnapshotError, BigMoney]] = {
    val rv = portfolioAssets.map { portfolioAsset =>
      assetRepo.findById(portfolioAsset.assetId).flatMap {
        case Some(asset) =>
          Future.successful(Right(BigMoney.of(CurrencyUnit.USD, 0)))
        case None => Future.successful(Left(PortfolioSnapshotError("No asset")))
      }
    }
    Future.successful(Right(BigMoney.of(CurrencyUnit.USD, 0)))
  }

  object PortfolioSnapshotServiceImpl {
    // Starting share price will always be $100. The starting share count
    // will be determined by NAV / $100
    private val initialSharePrice = BigMoney.of(CurrencyUnit.USD, 100)
  }
}
