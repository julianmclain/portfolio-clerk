package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class Portfolio(
    id: Long,
    userId: Long,
    shareCount: BigDecimal,
    sharePrice: MoneyWrapper,
    totalValue: MoneyWrapper
)

object Portfolio {
  implicit val portfolioFormat: OFormat[Portfolio] = Json.format[Portfolio]
}