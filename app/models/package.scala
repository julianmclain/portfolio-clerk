import org.joda.money.Money
import play.api.libs.json.Format
import play.api.libs.json.JsError
import play.api.libs.json.JsString
import play.api.libs.json.JsSuccess
import play.api.libs.json.Reads
import play.api.libs.json.Writes

package object models {
  implicit val moneyReads: Reads[Money] = {
    case JsString(str) => JsSuccess(Money.parse(str))
    case _             => JsError("error expected string")
  }
  implicit val moneyWrites: Writes[Money] = (money: Money) =>
    JsString(money.toString)
  implicit val moneyFormat: Format[Money] =
    Format(moneyReads, moneyWrites)
}
