package persistence.repositories

import persistence.tables.TransactionTableDefinition
import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import scala.concurrent.ExecutionContext

@Singleton
class TransactionRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends BaseRepository
    with TransactionTableDefinition {

  import profile.api._

}
