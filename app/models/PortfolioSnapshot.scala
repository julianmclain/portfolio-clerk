package models

import java.time.LocalDate
import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class PortfolioSnapshot(
    id: Long,
    portfolioId: Long,
    openingShareCount: BigDecimal,
    openingSharePrice: MoneyWrapper,
    openingValue: MoneyWrapper,
    netCashFlow: MoneyWrapper,
    cashFlowSharePrice: MoneyWrapper,
    numShareChange: BigDecimal,
    closingShareCount: BigDecimal,
    closingSharePrice: MoneyWrapper,
    closingValue: MoneyWrapper,
    netReturn: BigDecimal,
    date: LocalDate
)

object PortfolioSnapshot {
  implicit val portfolioSnapshotFormat: OFormat[PortfolioSnapshot] =
    Json.format[PortfolioSnapshot]
}
