package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class Withdrawal(
    id: Long,
    portfolioId: Long,
    totalAmount: Money,
    shareCountChange: BigDecimal
)

object Withdrawal {
  implicit val withdrawalFormat: OFormat[Withdrawal] =
    Json.format[Withdrawal]
}
