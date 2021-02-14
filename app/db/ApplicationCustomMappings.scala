package db

import models.AssetType
import models.Bond
import models.OptionContract
import models.Stock
import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import com.github.tminglei.slickpg.ExPostgresProfile.api._
import org.joda.money.Money

trait ApplicationCustomMappings {
  implicit val moneyMapper: JdbcType[Money] with BaseTypedType[Money] =
    MappedColumnType.base[Money, String](
      money => money.toString,
      str => Money.parse(str)
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
