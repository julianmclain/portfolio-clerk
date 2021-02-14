package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

import java.time.OffsetDateTime

case class Withdrawal(
    id: Long,
    portfolioId: Long,
    totalAmount: Money,
    shareCountChange: BigDecimal,
    withdrawalTimestamp: OffsetDateTime,
    createdAt: Option[OffsetDateTime],
    updatedAt: Option[OffsetDateTime]
)

object Withdrawal {
  implicit val withdrawalFormat: OFormat[Withdrawal] =
    Json.format[Withdrawal]
}
