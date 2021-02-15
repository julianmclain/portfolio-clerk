package integration.util

import com.typesafe.config.ConfigFactory
import org.scalatest.BeforeAndAfter
import org.scalatest.TestSuite
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.Configuration
import play.api.Mode
import play.api.db.Database
import play.api.inject.guice.GuiceApplicationBuilder
import repositories.UserRepository
import slick.basic.DatabaseConfig
import slick.dbio.DBIO
import slick.jdbc.PostgresProfile
import slick.lifted.TableQuery

trait PerSuiteTestApplication extends GuiceOneAppPerSuite with BeforeAndAfter {
  this: TestSuite =>

  private val testConfig = ConfigFactory.load("test-application.conf")
  implicit override lazy val app: Application = new GuiceApplicationBuilder()
    .configure(
      new Configuration(testConfig)
    )
    .in(Mode.Test)
    .build

  val dbConfig: DatabaseConfig[PostgresProfile] =
    DatabaseConfig.forConfig("default")
  val database = dbConfig.db

  before {
    println("before")
    // TODO - ideally run evolutions / create all the tables here
    val t: TableQuery[UserRepository#UserTable] =
      TableQuery[UserRepository#UserTable]

    // not sure why this doesn't work
//    database.run(t.filter(_.id === 1).result.headOption)
  }
  after {
    // TODO - ideally run downs / tear down all the tables here
    println("after")
  }
}
