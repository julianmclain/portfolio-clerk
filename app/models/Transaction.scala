package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

sealed trait AssetType
case object Stock extends AssetType
case object Bond extends AssetType
case object Option extends AssetType

case class Transaction(
    id: Long,
    assetType: AssetType,
    assetName: String,
    assetSymbol: String,
    quantity: Int,
    unitPriceCent: Long
)

object Transaction {
  implicit val transactionFormat: OFormat[Transaction] =
    Json.format[Transaction]
}
