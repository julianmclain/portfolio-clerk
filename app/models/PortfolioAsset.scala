package models

case class PortfolioAsset(
    id: Long,
    portfolioId: Long,
    assetId: Long,
    quantity: BigDecimal
)
