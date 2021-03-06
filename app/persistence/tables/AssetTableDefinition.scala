package persistence.tables

import models.Asset
import models.AssetSymbol
import models.AssetType
import play.api.db.slick.HasDatabaseConfigProvider
import slick.lifted.ProvenShape

trait AssetTableDefinition {
  this: HasDatabaseConfigProvider[ApplicationPostgresProfile] =>

  import profile.api._

  class AssetTable(tag: Tag)
      extends Table[Asset](tag, "assets")
      with AutoIncIdColumn
      with BaseTableDefinition {
    def assetName: Rep[String] = column[String]("asset_name")

    def assetSymbol: Rep[AssetSymbol] = column[AssetSymbol]("asset_symbol")

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

  def assets: TableQuery[AssetTable] = TableQuery[AssetTable]
}
