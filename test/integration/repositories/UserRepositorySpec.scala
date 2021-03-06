package integration.repositories

import integration.util.PerSuiteTestApplication
import models.User
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.Mode
import play.api.inject.guice.GuiceApplicationBuilder
import persistence.repositories.UserRepository

import java.time.OffsetDateTime
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class UserRepositorySpec extends PlaySpec with PerSuiteTestApplication {

  "UserRepository" should {

    val userRepo = app.injector.instanceOf[UserRepository]

    "create a new User" in {
      val user = Await.result(
        userRepo.create(
          User(
            1,
            "email",
            OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
            None,
            None
          )
        ),
        Duration.Inf
      )
      println(user)
      user.id mustBe 1
    }
  }

}
