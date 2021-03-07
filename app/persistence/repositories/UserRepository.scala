package persistence.repositories

import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import models.User
import persistence.tables.UserTableDefinition

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
class UserRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends BaseRepository
    with UserTableDefinition {

  import profile.api._

  def findById(id: Long): Future[Option[User]] =
    db.run(users.filter(_.id === id).result.headOption)

  def create(user: User): Future[User] = {
    val insertQuery =
      users returning users.map(_.id) into ((record, id) =>
        user.copy(
          id = id,
          updatedAt = record.updatedAt,
          createdAt = record.createdAt
        )
      )
    val action = insertQuery += user
    db.run(action)
  }
}
