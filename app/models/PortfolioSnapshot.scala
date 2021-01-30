package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class PortfolioSnapshot(
    id: Long,
    openingShareCount: Long,
    openingSharePrice: Long,
    openingValue: Long,
    netCashFlows: Long,
    cashFlowSharePrice: Long,
    numShareChange: Long,
    closingShareCount: Long,
    closingSharePrice: Long,
    closingValue: Long,
    netReturnPercentage: Long
)

object PortfolioSnapshot {
  implicit val portfolioSnapshotFormat: OFormat[PortfolioSnapshot] =
    Json.format[PortfolioSnapshot]
}
