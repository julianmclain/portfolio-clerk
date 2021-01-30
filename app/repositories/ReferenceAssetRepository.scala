package repositories

import play.api.db.slick.DatabaseConfigProvider
import javax.inject.Inject
import javax.inject.Singleton
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape
import models.ReferenceAsset

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
final class ReferenceAssetRepository @Inject() (
    dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

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
