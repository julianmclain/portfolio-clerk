package services

import models.Portfolio
import models.Transaction
import repositories.PortfolioRepository

import javax.inject.Inject
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

trait PortfolioService {

  /**
    * Create a new portfolio
    */
  def createPortfolio(portfolio: Portfolio): Future[Portfolio]

  /**
    * Deposit cash into a portfolio
    *
    * Depositing cash generates new shares so that the share price stays the same post-deposit. For example,
    * assume a portfolio with 1,000 shares at $1 each. Depositing $300 would generate 300 new shares. The
    * ending share count would be 1,300 and the ending share price would still be $1.
    */
  def makeCashDeposit(
      portfolio: Portfolio,
      transaction: Transaction
  ): Future[Transaction]

  /**
    *  Withdraw cash from a portfolio
    */
  def makeCashWithdrawal(
      portfolio: Portfolio,
      transaction: Transaction
  ): Future[Transaction]

  /**
    *  Buy or sell portfolio assets
    */
  def makeTransaction(
      portfolio: Portfolio,
      transaction: Transaction
  ): Future[Transaction]

}

class PortfolioServiceImpl @Inject() (
    portfolioRepo: PortfolioRepository
)(implicit ec: ExecutionContext)
    extends PortfolioService {

  override def createPortfolio(portfolio: Portfolio): Future[Portfolio] =
    portfolioRepo.create(portfolio)

  override def makeCashDeposit(
      portfolio: Portfolio,
      transaction: Transaction
  ): Future[Transaction] = ???

  override def makeCashWithdrawal(
      portfolio: Portfolio,
      transaction: Transaction
  ): Future[Transaction] = ???

  override def makeTransaction(
      portfolio: Portfolio,
      transaction: Transaction
  ): Future[Transaction] = ???
}
