package models

import org.joda.money.BigMoney
import play.api.libs.json.Json
import play.api.libs.json.OFormat

import java.time.OffsetDateTime

case class Transaction(
    id: Long,
    portfolioId: Long,
    portfolioAssetId: Long,
    quantity: BigDecimal,
    unitPrice: BigMoney,
    totalValue: BigMoney,
    transactionDatetime: OffsetDateTime,
    createdAt: Option[OffsetDateTime],
    updatedAt: Option[OffsetDateTime]
)

object Transaction {
  implicit val transactionFormat: OFormat[Transaction] =
    Json.format[Transaction]
}
