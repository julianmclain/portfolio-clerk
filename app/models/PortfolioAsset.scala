package models

import java.time.OffsetDateTime

case class PortfolioAsset(
    portfolioId: Long,
    assetId: Long,
    quantity: BigDecimal,
    createdAt: Option[OffsetDateTime],
    updatedAt: Option[OffsetDateTime]
)
