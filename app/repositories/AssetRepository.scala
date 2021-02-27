package repositories

import db.ApplicationPostgresProfile
import db.ApplicationPostgresProfile.api._
import db.AutoIncIdColumn
import db.TimestampColumns
import models.Asset
import models.AssetType
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import slick.lifted.ProvenShape

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
class AssetRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends HasDatabaseConfigProvider[ApplicationPostgresProfile] {

  private val assets = TableQuery[AssetTable]

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

private[repositories] class AssetTable(tag: Tag)
    extends Table[Asset](tag, "assets")
    with AutoIncIdColumn
    with TimestampColumns {
  def assetName: Rep[String] = column[String]("asset_name")
  def assetSymbol: Rep[String] = column[String]("asset_symbol")
  def assetType: Rep[AssetType] = column[AssetType]("asset_type")

  def * : ProvenShape[Asset] =
    (
      id,
      assetName,
      assetSymbol,
      assetType,
      createdAt,
      updatedAt
    ) <> ((Asset.apply _).tupled, Asset.unapply)
}
