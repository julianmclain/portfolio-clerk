package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

import java.time.OffsetDateTime

case class ReferenceAsset(
    id: Long,
    name: String,
    symbol: String,
    createdAt: Option[OffsetDateTime],
    updatedAt: Option[OffsetDateTime]
)

object ReferenceAsset {
  implicit val referenceAssetFormat: OFormat[ReferenceAsset] =
    Json.format[ReferenceAsset]
}
