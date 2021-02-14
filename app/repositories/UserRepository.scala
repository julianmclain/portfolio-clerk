package repositories

import db.ApplicationPostgresProfile
import db.AutoIncId
import db.Timestamps
import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import slick.lifted.ProvenShape
import models.User
import play.api.db.slick.HasDatabaseConfigProvider

import java.time.OffsetDateTime
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
class UserRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends HasDatabaseConfigProvider[ApplicationPostgresProfile] {

  import ApplicationPostgresProfile.api._

  private class UserTable(tag: Tag)
      extends Table[User](tag, "users")
      with AutoIncId
      with Timestamps {
    def email: Rep[String] = column[String]("email")
    def signupDatetime: Rep[OffsetDateTime] =
      column[OffsetDateTime]("signup_datetime")

    def * : ProvenShape[User] =
      (
        id,
        email,
        signupDatetime,
        createdAt,
        updatedAt
      ) <> ((User.apply _).tupled, User.unapply)
  }

  private val users = TableQuery[UserTable]

  def findById(id: Long): Future[Option[User]] =
    db.run(users.filter(_.id === id).result.headOption)

  def create(user: User): Future[User] = {
    val insertQuery =
      users returning users.map(_.id) into ((_, id) => user.copy(id = id))
    val action = insertQuery += user
    db.run(action)
  }
}
