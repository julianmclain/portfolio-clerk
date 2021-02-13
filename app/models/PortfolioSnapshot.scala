package models

import org.joda.time.DateTime

import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class PortfolioSnapshot(
    id: Long,
    portfolioId: Long,
    openingShareCount: BigDecimal,
    openingSharePrice: Money,
    openingValue: Money,
    netCashFlow: Money,
    numShareChange: BigDecimal,
    closingShareCount: BigDecimal,
    closingSharePrice: Money,
    closingValue: Money,
    netReturn: BigDecimal
//    snapshotDate: DateTime
)

object PortfolioSnapshot {
  implicit val portfolioSnapshotFormat: OFormat[PortfolioSnapshot] =
    Json.format[PortfolioSnapshot]
}
