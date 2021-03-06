package persistence

import com.github.tminglei.slickpg.ExPostgresProfile.api._
import models.AssetSymbol
import models.AssetType
import models.Bond
import models.OptionContract
import models.Stock
import org.joda.money.BigMoney
import slick.ast.BaseTypedType
import slick.jdbc.JdbcType

trait ApplicationCustomMappings {
  implicit val assetSymbolMapper
      : JdbcType[AssetSymbol] with BaseTypedType[AssetSymbol] =
    MappedColumnType.base[AssetSymbol, String](_.value, AssetSymbol)

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
