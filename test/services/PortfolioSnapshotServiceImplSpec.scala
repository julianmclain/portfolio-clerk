package services

import models.AssetSymbol
import models.Deposit
import models.Portfolio
import models.Withdrawal
import org.joda.money.BigMoney
import org.mockito.ArgumentMatchersSugar._
import org.mockito.IdiomaticMockito
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Helpers.baseApplicationBuilder.injector
import play.api.test.Injecting
import persistence.repositories.DepositRepository
import persistence.repositories.PortfolioAssetRepository
import persistence.repositories.PortfolioSnapshotRepository
import persistence.repositories.WithdrawalRepository

import java.time.LocalDate
import java.time.OffsetDateTime
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration

class PortfolioSnapshotServiceImplSpec
    extends PlaySpec
    with IdiomaticMockito
    with Injecting
    with GuiceOneAppPerTest {

  // Seems weird but taken from here: https://www.scalatest.org/user_guide/sharing_fixtures#getFixtureMethods
  def fixture =
    new {
      implicit lazy val ec: ExecutionContext =
        injector.instanceOf[ExecutionContext]
      val mockDepositRepo: DepositRepository = mock[DepositRepository]
      val mockWithdrawalRepo: WithdrawalRepository = mock[WithdrawalRepository]
      val mockPolygonClient: PolygonClient = mock[PolygonClient]
      val mockPortfolioSnapshotRepo: PortfolioSnapshotRepository =
        mock[PortfolioSnapshotRepository]
      val mockPortfolioAssetRepo: PortfolioAssetRepository =
        mock[PortfolioAssetRepository]
      val snapshotService = new PortfolioSnapshotServiceImpl(
        depositRepo = mockDepositRepo,
        withdrawalRepo = mockWithdrawalRepo,
        financialDataClient = mockPolygonClient,
        portfolioSnapshotRepo = mockPortfolioSnapshotRepo,
        portfolioAssetRepo = mockPortfolioAssetRepo
      )
      val portfolio: Portfolio =
        Portfolio(1, 1, "test portfolio", BigMoney.parse("USD 0"), None, None)
      val today: LocalDate = LocalDate.now()

    }

  "PortfolioSnapshotServiceImpl" should {
    "create the first snapshot of a portfolio" in {
      val f = fixture
      f.mockPortfolioSnapshotRepo.getLastSnapshot(f.portfolio) returns Future
        .successful(None)
      f.mockPortfolioAssetRepo.findAssetQuantities(
        f.portfolio.id
      ) returns Future
        .successful(
          Seq(
            (AssetSymbol("AAPL"), BigDecimal(99.99)),
            (AssetSymbol("SNOW"), BigDecimal(1))
          )
        )
      f.mockPolygonClient.getClosingStockPrice(
        AssetSymbol("AAPL"),
        any[LocalDate]
      ) returns Future.successful(Right(BigMoney.parse("USD 1")))
      f.mockPolygonClient.getClosingStockPrice(
        AssetSymbol("SNOW"),
        any[LocalDate]
      ) returns Future.successful(Right(BigMoney.parse("USD 2")))
      val datetime = OffsetDateTime.parse("2007-12-03T10:15:30+01:00")
      val despoit1 = Deposit(
        1,
        f.portfolio.id,
        BigMoney.parse("USD 10"),
        datetime,
        None,
        None
      )
      val despoit2 = Deposit(
        2,
        f.portfolio.id,
        BigMoney.parse("USD 10"),
        datetime,
        None,
        None
      )
      val withdrawal1 = Withdrawal(
        1,
        f.portfolio.id,
        BigMoney.parse("USD 1"),
        datetime,
        None,
        None
      )
      val withdrawal2 = Withdrawal(
        2,
        f.portfolio.id,
        BigMoney.parse("USD 3"),
        datetime,
        None,
        None
      )
      f.mockDepositRepo
        .findAllByPortfolioId(f.portfolio.id, None) returns Future
        .successful(Seq(despoit1, despoit2))
      f.mockWithdrawalRepo.findAllByPortfolioId(
        f.portfolio.id,
        None
      ) returns Future.successful(Seq(withdrawal1, withdrawal2))

      val snapshotOrError = Await.result(
        f.snapshotService
          .createPortfolioSnapshot(f.portfolio, OffsetDateTime.now()),
        Duration.Inf
      )
      snapshotOrError.isRight mustBe true
      snapshotOrError.map { snapshot =>
        snapshot.netCashFlow mustBe BigMoney.parse("USD 16")
        snapshot.closingValue mustBe BigMoney.parse("USD 101.99")
      }
    }

    "calculate a portfolio's net cash flow" in {
      val f = fixture
      val datetime = OffsetDateTime.parse("2007-12-03T10:15:30+01:00")
      val despoit1 = Deposit(
        1,
        f.portfolio.id,
        BigMoney.parse("USD 10"),
        datetime,
        None,
        None
      )
      val despoit2 = Deposit(
        2,
        f.portfolio.id,
        BigMoney.parse("USD 10"),
        datetime,
        None,
        None
      )
      val withdrawal1 = Withdrawal(
        1,
        f.portfolio.id,
        BigMoney.parse("USD 1"),
        datetime,
        None,
        None
      )
      val withdrawal2 = Withdrawal(
        2,
        f.portfolio.id,
        BigMoney.parse("USD 3"),
        datetime,
        None,
        None
      )
      f.mockDepositRepo
        .findAllByPortfolioId(f.portfolio.id, Some(datetime)) returns Future
        .successful(Seq(despoit1, despoit2))
      f.mockWithdrawalRepo.findAllByPortfolioId(
        f.portfolio.id,
        Some(datetime)
      ) returns Future.successful(Seq(withdrawal1, withdrawal2))
      val netCashFlow = Await.result(
        f.snapshotService.calculateNetCashFlow(f.portfolio, Some(datetime)),
        Duration.Inf
      )
      netCashFlow mustBe BigMoney.parse("USD 16")
    }

    "calculate a portfolio's net cash flow when no deposits or withdrawals " in {
      val f = fixture
      val datetime = OffsetDateTime.now()
      f.mockDepositRepo
        .findAllByPortfolioId(f.portfolio.id, Some(datetime)) returns Future
        .successful(Seq())
      f.mockWithdrawalRepo.findAllByPortfolioId(
        f.portfolio.id,
        Some(datetime)
      ) returns Future.successful(Seq())
      val netCashFlow = Await.result(
        f.snapshotService.calculateNetCashFlow(f.portfolio, Some(datetime)),
        Duration.Inf
      )
      netCashFlow mustBe BigMoney.parse("USD 0")

    }

    "calculate a portfolio's net asset value" in {
      val f = fixture
      f.mockPortfolioAssetRepo.findAssetQuantities(
        f.portfolio.id
      ) returns Future
        .successful(
          Seq(
            (AssetSymbol("AAPL"), BigDecimal(99.99)),
            (AssetSymbol("SNOW"), BigDecimal(1))
          )
        )
      f.mockPolygonClient.getClosingStockPrice(
        AssetSymbol("AAPL"),
        any[LocalDate]
      ) returns Future.successful(Right(BigMoney.parse("USD 1")))
      f.mockPolygonClient.getClosingStockPrice(
        AssetSymbol("SNOW"),
        any[LocalDate]
      ) returns Future.successful(Right(BigMoney.parse("USD 2")))

      val result = Await.result(
        f.snapshotService.calculateNetAssetValue(f.portfolio, f.today),
        Duration.Inf
      )

      result mustBe Right(BigMoney.parse("USD 101.99"))
    }
  }
}
