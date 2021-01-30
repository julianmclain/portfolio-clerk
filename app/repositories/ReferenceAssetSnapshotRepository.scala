package repositories

import play.api.db.slick.DatabaseConfigProvider
import javax.inject.Inject
import javax.inject.Singleton
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape
import models.ReferenceAssetSnapshot
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
class ReferenceAssetSnapshotRepository @Inject() (
    dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class ReferenceAssetSnapshotTable(tag: Tag)
      extends Table[ReferenceAssetSnapshot](tag, "referenceAsset_snapshots") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def * : ProvenShape[ReferenceAssetSnapshot] =
      (id).mapTo[ReferenceAssetSnapshot]
  }
}
