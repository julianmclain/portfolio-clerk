package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

sealed trait AssetType
case object Stock extends AssetType
case object Bond extends AssetType
case object OptionContract extends AssetType
case object Cash extends AssetType {
  val ID = 1
}

case class Asset(
    id: Long,
    portfolioId: Long,
    assetName: String,
    assetSymbol: String,
    assetType: AssetType
)

// TODO - need to figure out json serialization for AssetType. Probably easier to just use beachape enunm,
//  but I should do some research
//object Asset {
//  implicit val assetFormat: OFormat[Asset] = Json.format[Asset]
//}
