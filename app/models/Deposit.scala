package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class Deposit(
    id: Long,
    portfolioId: Long,
    totalAmount: Money
)

object Deposit {
  implicit val depositFormat: OFormat[Deposit] = Json.format[Deposit]
}
