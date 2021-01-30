package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class User(
    id: Long,
    email: Long
)

object User {
  implicit val userFormat: OFormat[User] = Json.format[User]
}
