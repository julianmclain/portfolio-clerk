package models

import org.joda.money.BigMoney
import play.api.libs.json.Json
import play.api.libs.json.OFormat

import java.time.OffsetDateTime

case class Portfolio(
    id: Long,
    userId: Long,
    name: String,
    cashBalance: BigMoney,
    createdAt: Option[OffsetDateTime],
    updatedAt: Option[OffsetDateTime]
)

object Portfolio {
  implicit val portfolioFormat: OFormat[Portfolio] = Json.format[Portfolio]
}
