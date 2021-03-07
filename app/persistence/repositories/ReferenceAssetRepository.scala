package persistence.repositories

import persistence.tables.ReferenceAssetTableDefinition
import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import scala.concurrent.ExecutionContext

@Singleton
class ReferenceAssetRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends BaseRepository
    with ReferenceAssetTableDefinition {

  import profile.api._

}
