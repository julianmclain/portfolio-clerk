package models

import org.joda.money.Money
import play.api.libs.json.Json
import play.api.libs.json.OFormat

import java.time.OffsetDateTime

case class Transaction(
    id: Long,
    portfolioId: Long,
    portfolioAssetId: Long,
    quantity: BigDecimal,
    unitPrice: Money,
    totalValue: Money,
    transactionDatetime: OffsetDateTime,
    createdAt: Option[OffsetDateTime],
    updatedAt: Option[OffsetDateTime]
)

object Transaction {
  implicit val transactionFormat: OFormat[Transaction] =
    Json.format[Transaction]
}
