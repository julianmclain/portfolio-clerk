import org.joda.money.BigMoney
import play.api.libs.json.Format
import play.api.libs.json.JsError
import play.api.libs.json.JsString
import play.api.libs.json.JsSuccess
import play.api.libs.json.Reads
import play.api.libs.json.Writes

package object models {
  implicit val bigMoneyReads: Reads[BigMoney] = {
    case JsString(str) => JsSuccess(BigMoney.parse(str))
    case _             => JsError("error expected string")
  }
  implicit val bigMoneyWrites: Writes[BigMoney] = (bigMoney: BigMoney) =>
    JsString(bigMoney.toString)
  implicit val bigMoneyFormat: Format[BigMoney] =
    Format(bigMoneyReads, bigMoneyWrites)
}
