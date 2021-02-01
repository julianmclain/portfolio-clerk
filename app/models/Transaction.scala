package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class Transaction(
    id: Long,
    assetId: Long,
    portfolioId: Long,
    assetName: String,
    assetSymbol: String,
    assetType: AssetType,
    quantity: Int,
    unitPrice: MoneyWrapper,
    totalValue: MoneyWrapper
)

// TODO - need to fix json serialization for AssetType first
//object Transaction {
//  implicit val transactionFormat: OFormat[Transaction] =
//    Json.format[Transaction]
//}
