package persistence.repositories

import persistence.ApplicationPostgresProfile
import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import play.api.db.slick.HasDatabaseConfigProvider

import scala.concurrent.ExecutionContext

@Singleton
class TransactionRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends HasDatabaseConfigProvider[ApplicationPostgresProfile] {

  import profile.api._

}
