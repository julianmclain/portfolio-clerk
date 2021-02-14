package models

import org.joda.money.Money
import play.api.libs.json.Json
import play.api.libs.json.OFormat

import java.time.OffsetDateTime

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
    netReturn: BigDecimal,
    snapshotDate: OffsetDateTime,
    createdAt: Option[OffsetDateTime],
    updatedAt: Option[OffsetDateTime]
)

object PortfolioSnapshot {
  implicit val portfolioSnapshotFormat: OFormat[PortfolioSnapshot] =
    Json.format[PortfolioSnapshot]
}
