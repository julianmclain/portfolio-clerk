package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class PortfolioSnapshot(
    id: Long,
    date: String,
    openingShareCount: Long,
    openingSharePrice: Long,
    openingValue: Long,
    netCashFlows: Long,
    cashFlowSharePrice: Long,
    numShareChange: Long,
    closingValue: Long,
    closingShares: Long,
    closingSharePrice: Long,
    netReturnPercentage: Long
)

object PortfolioSnapshot {
  implicit val portfolioSnapshotFormat: OFormat[PortfolioSnapshot] =
    Json.format[PortfolioSnapshot]
}
