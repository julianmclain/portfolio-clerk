package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class Transaction(
    id: Long,
    portfolioId: Long,
    portfolioAssetId: Long,
    quantity: BigDecimal,
    unitPrice: Money,
    totalValue: Money
)

object Transaction {
  implicit val transactionFormat: OFormat[Transaction] =
    Json.format[Transaction]
}
