package models

import org.joda.money.BigMoney
import play.api.libs.json.Json
import play.api.libs.json.OFormat

import java.time.OffsetDateTime

case class PortfolioSnapshot(
    id: Long,
    portfolioId: Long,
    openingShareCount: BigDecimal,
    openingSharePrice: BigMoney,
    openingValue: BigMoney,
    netCashFlow: BigMoney,
    numShareChange: BigDecimal,
    closingShareCount: BigDecimal,
    closingSharePrice: BigMoney,
    closingValue: BigMoney,
    netReturn: BigDecimal,
    snapshotDatetime: OffsetDateTime,
    createdAt: Option[OffsetDateTime],
    updatedAt: Option[OffsetDateTime]
)

object PortfolioSnapshot {
  implicit val portfolioSnapshotFormat: OFormat[PortfolioSnapshot] =
    Json.format[PortfolioSnapshot]
}
