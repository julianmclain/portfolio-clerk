package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

import java.time.OffsetDateTime

sealed trait AssetType
case object Stock extends AssetType
case object Bond extends AssetType
case object OptionContract extends AssetType

case class AssetSymbol(value: String) extends AnyVal

case class Asset(
    id: Long,
    name: String,
    assetSymbol: AssetSymbol,
    assetType: AssetType,
    createdAt: Option[OffsetDateTime],
    updatedAt: Option[OffsetDateTime]
)

// TODO - need to figure out json serialization for AssetType. Probably easier to just use beachape enunm,
//  but I should do some research
//object Asset {
//  implicit val assetFormat: OFormat[Asset] = Json.format[Asset]
//}
