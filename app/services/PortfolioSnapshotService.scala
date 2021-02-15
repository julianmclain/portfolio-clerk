//package services
//
//import models.Portfolio
//import models.PortfolioAsset
//import models.PortfolioSnapshot
//import org.joda.money.Money
//import repositories.PortfolioAssetRepository
//import repositories.PortfolioRepository
//import repositories.PortfolioSnapshotRepository
//import util.GlobalConstants.ROUNDING_MODE
//
//import java.time.OffsetDateTime
//import javax.inject.Inject
//import scala.concurrent.ExecutionContext
//import scala.concurrent.Future
//import scala.math.BigDecimal.javaBigDecimal2bigDecimal
//
//trait PortfolioSnapshotService {
//
//  /**
//    * Calculate a point in time snapshot of the portfolio value
//    */
//  def createPortfolioSnapshot(
//      portfolio: Portfolio
//  ): Future[PortfolioSnapshot]
//
//}
//
//class PortfolioSnapshotServiceImpl @Inject() (
//    portfolioRepo: PortfolioRepository,
//    portfolioSnapshotRepo: PortfolioSnapshotRepository,
//    portfolioAssetRepo: PortfolioAssetRepository
//)(implicit ec: ExecutionContext)
//    extends PortfolioService {
//
//  /**
//    * v1 assumes that this method will be called 1 time per day once after-hours trading closes.
//    *
//    * To calculate a portfolio snapshot:
//    *  1. fetch all portfolio assets
//    *  2. for each portfolio asset, calculate the current value
//    *  3. fetch all deposit / withdrawals since the last snapshot
//    *  4. run the calculation
//    *     - opening values taken from portfolio record last snapshot value
//    *     - netCashFlow and numShareChange comes from any deposits / withdrawals
//    *     - closingShareCount = openingShareCount + numSharesChange
//    *     - closingValue = openingValue + change in value for every asset in the portfolio
//    *     - closingSharePrice = closingValue / shareCount
//    *     - netReturn = (closingSharePrice - openingSharePrice) / 100
//    */
//  def createPortfolioSnapshot(
//      portfolio: Portfolio,
//      currentDatetime: OffsetDateTime
//  ): Future[PortfolioSnapshot] =
//    for {
//      lastSnapshot <- getLastSnapshot(portfolio)
//      portfolioAssets <- portfolioAssetRepo.findAllByPortfolioId(portfolio.id)
//      netAssetValue <- calculateNetAssetValue(portfolioAssets)
//      (netCashFlow, numShareChange) =
//        calculateNetCashFlow(portfolio, lastSnapshot.snapshotDatetime)
//      openingSharePrice = lastSnapshot.closingSharePrice
//      currentShareCount = lastSnapshot.closingShareCount + numShareChange
//      closingSharePrice =
//        netAssetValue.dividedBy(currentShareCount.bigDecimal, ROUNDING_MODE)
//
//    } yield {
//      portfolioSnapshotRepo.create(
//        PortfolioSnapshot(
//          id = 0,
//          portfolioId = portfolio.id,
//          openingShareCount = lastSnapshot.closingShareCount,
//          openingSharePrice = openingSharePrice,
//          openingValue = lastSnapshot.closingValue,
//          netCashFlow = netCashFlow,
//          numShareChange = numShareChange,
//          closingShareCount = currentShareCount,
//          closingSharePrice = closingSharePrice,
//          closingValue = netAssetValue,
//          netReturn =
//            closingSharePrice.minus(openingSharePrice).getAmount / 100,
//          snapshotDatetime = currentDatetime,
//          None,
//          None
//        )
//      )
//    }
//
//  private def calculateNetCashFlow(
//      portfolio: Portfolio,
//      since: OffsetDateTime
//  ): (Money, BigDecimal) =
//    ???
//
//  private def calculateNetAssetValue(
//      assets: Seq[PortfolioAsset]
//  ): Future[Money] = ???
//
//  private def getLastSnapshot(
//      portfolioId: Portfolio
//  ): Future[PortfolioSnapshot] =
//    ???
//}
