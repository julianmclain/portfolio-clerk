package db

import ApplicationPostgresProfile.api._
import slick.relational.RelationalProfile

import java.time.OffsetDateTime

trait Timestamps { self: RelationalProfile#Table[_] =>
  def createdAt: Rep[Option[OffsetDateTime]] =
    column[Option[OffsetDateTime]]("created_at", O.AutoInc)
  def updatedAt: Rep[Option[OffsetDateTime]] =
    column[Option[OffsetDateTime]]("updated_at", O.AutoInc)
}
