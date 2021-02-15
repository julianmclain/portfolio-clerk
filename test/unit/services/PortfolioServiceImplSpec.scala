//package unit.services
//
//import org.mockito.IdiomaticMockito
//import org.scalatestplus.play.PlaySpec
//import org.scalatestplus.play.guice.GuiceOneAppPerTest
//import play.api.test.Helpers.baseApplicationBuilder.injector
//import play.api.test.Injecting
//import repositories.DepositRepository
//import repositories.PortfolioAssetRepository
//import repositories.PortfolioRepository
//import repositories.PortfolioSnapshotRepository
//
//import scala.concurrent.ExecutionContext
//
//class PortfolioServiceImplSpec
//    extends PlaySpec
//    with GuiceOneAppPerTest
//    with Injecting
//    with IdiomaticMockito {
//
//  "PortfolioSnapshotService" should {
//
//    "calculate portfolio snapshot" in {
//      implicit lazy val ec: ExecutionContext =
//        injector.instanceOf[ExecutionContext]
//      val mockPortfolioRepo = mock[PortfolioRepository]
//      val mockDepositRepo = mock[DepositRepository]
//      val mockPortfolioSnapshotRepo = mock[PortfolioSnapshotRepository]
//      val mockPortfolioAssetRepo = mock[PortfolioAssetRepository]
//
//      true mustBe true
//    }
//  }
//}
