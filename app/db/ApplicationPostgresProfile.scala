package db

import com.github.tminglei.slickpg.ExPostgresProfile
import com.github.tminglei.slickpg.PgDate2Support

trait ApplicationPostgresProfile extends ExPostgresProfile with PgDate2Support {
  override val api = new API
    with DateTimeImplicits
    with ApplicationCustomMappings

}

object ApplicationPostgresProfile extends ApplicationPostgresProfile {}
