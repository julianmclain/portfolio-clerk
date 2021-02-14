package db

import ApplicationPostgresProfile.api._
import slick.relational.RelationalProfile

trait AutoIncId { self: RelationalProfile#Table[_] =>
  final def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
}
