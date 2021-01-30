package models

import org.joda.money.Money
import play.api.libs.json.Format
import play.api.libs.json.JsError
import play.api.libs.json.JsString
import play.api.libs.json.JsSuccess
import play.api.libs.json.Reads
import play.api.libs.json.Writes

class MoneyWrapper(moneyStr: String) {
  private val jodaMoney = org.joda.money.Money.parse(moneyStr)

  def getInstance: Money = jodaMoney

  override def toString: String = jodaMoney.toString
}

object MoneyWrapper {
  def apply(moneyStr: String) = new MoneyWrapper(moneyStr)

  implicit val moneyReads: Reads[MoneyWrapper] = {
    case JsString(moneyStr) => JsSuccess(MoneyWrapper(moneyStr))
    case _                  => JsError("error expected string")
  }

  implicit val moneyWrites: Writes[MoneyWrapper] = (m: MoneyWrapper) =>
    JsString(m.toString)

  implicit val moneyFormat: Format[MoneyWrapper] =
    Format(moneyReads, moneyWrites)
}
