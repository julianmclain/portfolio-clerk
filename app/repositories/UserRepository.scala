package repositories

import play.api.db.slick.DatabaseConfigProvider
import javax.inject.Inject
import javax.inject.Singleton
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape
import models.User

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
class UserRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(
    implicit ec: ExecutionContext
) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class UserTable(tag: Tag) extends Table[User](tag, "users") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def email: Rep[String] = column[String]("email")

    def * : ProvenShape[User] = (id, email).mapTo[User]
  }
}
