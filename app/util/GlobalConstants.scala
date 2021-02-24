package util

import java.math.RoundingMode
import java.time.ZoneOffset

object GlobalConstants {
  val GLOBAL_ROUNDING_MODE = RoundingMode.HALF_UP
  val GLOBAL_TZ_OFFSET = ZoneOffset.UTC
}
