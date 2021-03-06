package persistence.tables

import slick.relational.RelationalProfile
import persistence.ApplicationPostgresProfile.api._

trait AutoIncIdColumn { self: RelationalProfile#Table[_] =>
  final def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
}
