package persistence.tables

import models.User
import persistence.ApplicationPostgresProfile
import play.api.db.slick.HasDatabaseConfigProvider
import slick.lifted.ProvenShape

import java.time.OffsetDateTime

trait UserTableDefinition {
  this: HasDatabaseConfigProvider[ApplicationPostgresProfile] =>

  import profile.api._

  class UserTable(tag: Tag)
      extends Table[User](tag, "users")
      with AutoIncIdColumn
      with TimestampColumns {
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

  def users: TableQuery[UserTable] = TableQuery[UserTable]
}
