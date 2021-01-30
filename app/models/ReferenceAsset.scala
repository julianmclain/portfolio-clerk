package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class ReferenceAsset(id: Long, name: String, symbol: String)

object ReferenceAsset {
  implicit val referenceAssetFormat: OFormat[ReferenceAsset] =
    Json.format[ReferenceAsset]
}
