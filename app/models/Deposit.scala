package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class Deposit(
    id: Long,
    amount: MoneyWrapper,
    portfolioId: Long
)

object Deposit {
  implicit val depositFormat: OFormat[Deposit] = Json.format[Deposit]
}
