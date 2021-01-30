package repositories

import org.joda.money.Money
import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.jdbc.PostgresProfile.api._

object CustomColumnTypes {
  implicit val moneyTypeMapper: JdbcType[Money] with BaseTypedType[Money] =
    MappedColumnType.base[Money, String](
      money => money.toString,
      str => Money.parse(str)
    )
}
