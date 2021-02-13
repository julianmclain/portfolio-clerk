package repositories

import com.github.tminglei.slickpg.ExPostgresProfile
import com.github.tminglei.slickpg.PgDate2Support

trait ApplicationPostgresProfile extends ExPostgresProfile with PgDate2Support {
//  DateTimeImplicits
}

object ApplicationPostgresProfile extends ApplicationPostgresProfile
