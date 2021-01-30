package repositories

import models.MoneyWrapper
import org.joda.money.Money
import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.jdbc.PostgresProfile.api._

object CustomColumnTypes {
  implicit val moneyTypeMapper
      : JdbcType[MoneyWrapper] with BaseTypedType[MoneyWrapper] =
    MappedColumnType.base[MoneyWrapper, String](
      money => money.toString,
      str => MoneyWrapper(str)
    )
}
