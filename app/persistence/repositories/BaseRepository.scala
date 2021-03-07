package persistence.repositories

import persistence.ApplicationPostgresProfile
import play.api.db.slick.HasDatabaseConfigProvider

trait BaseRepository
    extends HasDatabaseConfigProvider[ApplicationPostgresProfile]
