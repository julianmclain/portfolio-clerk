package persistence.tables

import models.ReferenceAsset
import play.api.db.slick.HasDatabaseConfigProvider
import slick.lifted.ProvenShape

trait ReferenceAssetTableDefinition {
  this: HasDatabaseConfigProvider[ApplicationPostgresProfile] =>

  import profile.api._

  class ReferenceAssetTable(tag: Tag)
      extends Table[ReferenceAsset](tag, "reference_assets")
      with AutoIncIdColumn
      with BaseTableDefinition {
    def name: Rep[String] = column[String]("name")
    def symbol: Rep[String] = column[String]("symbol")
    def * : ProvenShape[ReferenceAsset] =
      (id, name, symbol, createdAt, updatedAt) <> (
        (ReferenceAsset.apply _).tupled,
        ReferenceAsset.unapply
      )
  }

}
