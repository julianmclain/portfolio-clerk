package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

import java.time.OffsetDateTime

case class User(
    id: Long,
    email: String,
    signupDatetime: OffsetDateTime,
    createdAt: Option[OffsetDateTime],
    updatedAt: Option[OffsetDateTime]
)

object User {
  implicit val userFormat: OFormat[User] = Json.format[User]
}
