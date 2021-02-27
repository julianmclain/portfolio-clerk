package db

import models.AssetType
import models.Bond
import models.OptionContract
import models.Stock
import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import com.github.tminglei.slickpg.ExPostgresProfile.api._
import org.joda.money.BigMoney
import org.joda.money.Money

trait ApplicationCustomMappings {
  implicit val bigMoneyMapper: JdbcType[BigMoney] with BaseTypedType[BigMoney] =
    MappedColumnType.base[BigMoney, String](
      bigMoney => bigMoney.toString,
      str => BigMoney.parse(str)
    )

  implicit val assetTypeMapper
      : JdbcType[AssetType] with BaseTypedType[AssetType] =
    MappedColumnType.base[AssetType, String](
      at => at.getClass.getSimpleName,
      {
        case "Stock"  => Stock
        case "Bond"   => Bond
        case "Option" => OptionContract
      }
    )
}
