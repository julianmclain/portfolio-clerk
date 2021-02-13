package models

import play.api.libs.json.Format
import play.api.libs.json.JsError
import play.api.libs.json.JsString
import play.api.libs.json.JsSuccess
import play.api.libs.json.Reads
import play.api.libs.json.Writes

import java.math.RoundingMode

class Money(moneyStr: String) {

  private val jodaMoney = org.joda.money.BigMoney.parse(moneyStr)

  private def getInstance: org.joda.money.BigMoney = jodaMoney

  def dividedBy(value: BigDecimal): Money = {
    val result =
      this.getInstance.dividedBy(value.bigDecimal, RoundingMode.UNNECESSARY)
    Money(result.toString)
  }

  def minus(value: Money): Money = {
    val result = this.getInstance.minus(value.getInstance)
    Money(result.toString)
  }

  def getAmount: BigDecimal = this.getInstance.getAmount

  override def toString: String = jodaMoney.toString
}

object Money {
  def apply(moneyStr: String) = new Money(moneyStr)

  implicit val moneyReads: Reads[Money] = {
    case JsString(moneyStr) => JsSuccess(Money(moneyStr))
    case _                  => JsError("error expected string")
  }

  implicit val moneyWrites: Writes[Money] = (m: Money) => JsString(m.toString)

  implicit val moneyFormat: Format[Money] =
    Format(moneyReads, moneyWrites)
}
