package persistence.repositories

import models.Asset
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import persistence.tables.ApplicationPostgresProfile

import javax.inject.Inject
import javax.inject.Singleton
import persistence.tables.AssetTableDefinition

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
class AssetRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends HasDatabaseConfigProvider[ApplicationPostgresProfile]
    with AssetTableDefinition {
  import profile.api._

  def findById(id: Long): Future[Option[Asset]] =
    db.run(assets.filter(_.id === id).result.headOption)

  def create(asset: Asset): Future[Asset] = {
    val insertQuery =
      assets returning assets.map(_.id) into ((record, id) =>
        asset.copy(
          id = id,
          updatedAt = record.updatedAt,
          createdAt = record.createdAt
        )
      )
    val action = insertQuery += asset
    db.run(action)
  }
}
