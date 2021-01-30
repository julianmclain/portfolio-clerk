package models

import org.joda.money.Money
import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class Deposit(
    id: Long,
    amount: Money,
    portfolioId: Long
)

//object Deposit {
//  implicit val depositFormat: OFormat[Deposit] = Json.format[Deposit]
//}
