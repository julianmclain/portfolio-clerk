package persistence.services

import com.google.common.annotations.VisibleForTesting
import models.FinancialDataClient
import models.Portfolio
import models.PortfolioSnapshot
import models.PortfolioSnapshotError
import org.joda.money.BigMoney
import org.joda.money.CurrencyUnit
import persistence.repositories.DepositRepository
import persistence.repositories.PortfolioAssetRepository
import persistence.repositories.PortfolioSnapshotRepository
import persistence.repositories.WithdrawalRepository
import util.GlobalConstants.GLOBAL_ROUNDING_MODE

import java.time.LocalDate
import java.time.OffsetDateTime
import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.math.BigDecimal.javaBigDecimal2bigDecimal

class PostgresPortfolioSnapshotService @Inject() (
    depositRepo: DepositRepository,
    withdrawalRepo: WithdrawalRepository,
    financialDataClient: FinancialDataClient,
    portfolioSnapshotRepo: PortfolioSnapshotRepository,
    portfolioAssetRepo: PortfolioAssetRepository
)(implicit ec: ExecutionContext)
    extends PortfolioSnapshotService {

  /**
    *
    * This v1 implementation assumes that it will be called 1 time per day once after-hours trading is done.
    *
    * To calculate a portfolio snapshot:
    *  1. fetch all portfolio assets
    *     2. for each portfolio asset, calculate the current value
    *     3. fetch all deposit / withdrawals since the last snapshot
    *     4. run the calculation
    *     - opening values taken from portfolio record last snapshot value
    *     - netCashFlow and numShareChange comes from any deposits / withdrawals
    *     - closingShareCount = openingShareCount + numSharesChange
    *     - closingValue = openingValue + change in value for every asset in the portfolio
    *     - closingSharePrice = closingValue / shareCount
    *     - netReturn = (closingSharePrice - openingSharePrice) / 100
    */
  def create(
      portfolio: Portfolio,
      currentDatetime: OffsetDateTime
  ): Future[Either[PortfolioSnapshotError, PortfolioSnapshot]] =
    portfolioSnapshotRepo.getLastSnapshot(portfolio).flatMap {
      case Some(prev) =>
        createIncrementalSnapshot(portfolio, prev, currentDatetime)
      case None => createFirstSnapshot(portfolio, currentDatetime)
    }

  /**
    * The goal of the first snapshot is to open the portfolio at a share price of [[initialSharePrice]].
    * Originally I started with a fixed share count, but that creates a situation where the share price
    * is annoyingly small if portfolios are created with a small deposit. I think its better to have a
    * more human friendly share price than share count.
    */
  @VisibleForTesting
  private def createFirstSnapshot(
      portfolio: Portfolio,
      currentDatetime: OffsetDateTime
  ): Future[Either[PortfolioSnapshotError, PortfolioSnapshot]] =
    calculateNetAssetValue(portfolio, currentDatetime.toLocalDate).flatMap {
      navOrError =>
        calculateNetCashFlow(portfolio).flatMap { netCashFlow =>
          navOrError match {
            case Right(nav) => {
              val shareCount = nav.getAmount / initialSharePrice.getAmount
              val sharePrice = {
                if (shareCount > 0) initialSharePrice
                else BigMoney.of(CurrencyUnit.USD, 0)
              }
              portfolioSnapshotRepo
                .create(
                  PortfolioSnapshot(
                    id = 0,
                    portfolioId = portfolio.id,
                    openingShareCount = shareCount,
                    openingSharePrice = sharePrice,
                    openingValue = nav,
                    netCashFlow = netCashFlow,
                    numShareChange = shareCount,
                    closingShareCount = shareCount,
                    closingSharePrice = sharePrice,
                    closingValue = nav,
                    netReturn = BigDecimal(0),
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
    }

  private def createIncrementalSnapshot(
      portfolio: Portfolio,
      prevSnapshot: PortfolioSnapshot,
      currentDatetime: OffsetDateTime
  ): Future[Either[PortfolioSnapshotError, PortfolioSnapshot]] = {
    calculateNetAssetValue(portfolio, currentDatetime.toLocalDate).flatMap {
      navOrError =>
        calculateNetCashFlow(portfolio, Some(prevSnapshot.snapshotDatetime))
          .flatMap { netCashFlow =>
            navOrError match {
              case Right(nav) => {
                val openingSharePrice = prevSnapshot.closingSharePrice
                val numShareChange =
                  netCashFlow.getAmount / openingSharePrice.getAmount
                val currentShareCount =
                  prevSnapshot.closingShareCount + numShareChange
                val currentSharePrice = nav.dividedBy(
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
                      closingSharePrice = currentSharePrice,
                      closingValue = nav,
                      netReturn =
                        currentSharePrice
                          .minus(openingSharePrice)
                          .getAmount / 100,
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
    }
  }

  @VisibleForTesting
  private[services] def calculateNetCashFlow(
      portfolio: Portfolio,
      since: Option[OffsetDateTime] = None
  ): Future[BigMoney] = {
    for {
      deposits <- depositRepo.findAllByPortfolioId(portfolio.id, since)
      withdrawals <- withdrawalRepo.findAllByPortfolioId(portfolio.id, since)
      depositsTotal =
        deposits.foldLeft(BigMoney.of(CurrencyUnit.USD, 0))((sum, deposit) =>
          sum.plus(deposit.totalAmount)
        )
      withdrawalsTotal = withdrawals.foldLeft(BigMoney.of(CurrencyUnit.USD, 0))(
        (sum, withdrawal) => sum.plus(withdrawal.totalAmount)
      )
    } yield depositsTotal.minus(withdrawalsTotal)
  }

  @VisibleForTesting
  private[services] def calculateNetAssetValue(
      portfolio: Portfolio,
      date: LocalDate
  ): Future[Either[PortfolioSnapshotError, BigMoney]] = {
    val netAssetValueOrErrorFt = for {
      assetQuantities <- portfolioAssetRepo.findAssetQuantities(portfolio.id)
      assetValuesOrErrors <- Future.sequence {
        assetQuantities.map(assetQuantity => {
          val assetValue = financialDataClient
            .getClosingStockPrice(assetQuantity._1, date)
            .map {
              case Right(price) =>
                Right(price.multipliedBy(assetQuantity._2.bigDecimal))
              case Left(error) => Left(error)
            }
          assetValue
        })
      }
    } yield assetValuesOrErrors collectFirst {
      case Left(error) => Left(error)
    } getOrElse {
      val assetValues = assetValuesOrErrors collect { case Right(x) => x }
      val sum = assetValues.foldLeft(BigMoney.of(CurrencyUnit.USD, 0))(
        (sum, assetValue) => sum.plus(assetValue)
      )
      Right(sum)
    }
    netAssetValueOrErrorFt
  }
}
