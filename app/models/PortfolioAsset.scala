package models

case class PortfolioAsset(
    portfolioId: Long,
    assetId: Long,
    quantity: BigDecimal
)
