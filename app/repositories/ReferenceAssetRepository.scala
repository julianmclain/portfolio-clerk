package repositories

import db.ApplicationPostgresProfile
import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import slick.lifted.ProvenShape
import models.ReferenceAsset
import play.api.db.slick.HasDatabaseConfigProvider

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
class ReferenceAssetRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends HasDatabaseConfigProvider[ApplicationPostgresProfile] {

  import ApplicationPostgresProfile.api._

  private class ReferenceAssetTable(tag: Tag)
      extends Table[ReferenceAsset](tag, "reference_assets") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name: Rep[String] = column[String]("name")
    def symbol: Rep[String] = column[String]("symbol")

    def * : ProvenShape[ReferenceAsset] =
      (id, name, symbol) <> (
        (ReferenceAsset.apply _).tupled,
        ReferenceAsset.unapply
      )
  }
}
