package models

sealed trait PortfolioSnapshotError {
  def message: String
}

case class FinancialDataClientError(message: String)
    extends PortfolioSnapshotError
