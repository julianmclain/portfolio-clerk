package models

import org.joda.money.BigMoney
import play.api.libs.json.Json
import play.api.libs.json.OFormat

import java.time.OffsetDateTime

case class Deposit(
    id: Long,
    portfolioId: Long,
    totalAmount: BigMoney,
    depositDatetime: OffsetDateTime,
    createdAt: Option[OffsetDateTime],
    updatedAt: Option[OffsetDateTime]
)

object Deposit {
  implicit val depositFormat: OFormat[Deposit] = Json.format[Deposit]
}
