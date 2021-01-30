package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class ReferenceAssetSnapshot(
    id: Long,
    value: Long,
    sharePrice: Long,
    dailyReturnPercentage: Long
)

object ReferenceAssetSnapshot {
  implicit val referenceAssetSnapshotFormat: OFormat[ReferenceAssetSnapshot] =
    Json.format[ReferenceAssetSnapshot]
}
