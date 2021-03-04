package repositories

import db.ApplicationPostgresProfile
import db.ApplicationPostgresProfile.api._
import db.AutoIncIdColumn
import db.TimestampColumns
import models.Asset
import models.Deposit
import org.joda.money.BigMoney
import play.api.db.slick.DatabaseConfigProvider
import slick.lifted.ProvenShape

import javax.inject.Inject
import javax.inject.Singleton
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import play.api.db.slick.HasDatabaseConfigProvider
import slick.dbio.Effect
import slick.sql.FixedSqlAction

import java.time.OffsetDateTime

@Singleton
class DepositRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends HasDatabaseConfigProvider[ApplicationPostgresProfile] {

  private val deposits = TableQuery[DepositTable]
  private val portfolioAssets = TableQuery[PortfolioAssetTable]
  private val assets = TableQuery[AssetTable]

  def test(deposit: Deposit, asset: Asset) = {
    val insertDeposit =
      deposits returning deposits.map(_.id) into ((record, id) =>
        deposit.copy(
          id = id,
          updatedAt = record.updatedAt,
          createdAt = record.createdAt
        )
      ) += deposit

    val insertAsset =
      assets returning assets.map(_.id) into ((record, id) =>
        asset.copy(
          id = id,
          updatedAt = record.updatedAt,
          createdAt = record.createdAt
        )
      ) += asset

    db.run(insertDeposit andThen insertAsset)
  }

  def findById(id: Long): Future[Option[Deposit]] =
    db.run(deposits.filter(_.id === id).result.headOption)

  def findAllByPortfolioId(
      portfolioId: Long,
      since: Option[OffsetDateTime] = None
  ): Future[Seq[Deposit]] =
    since match {
      case Some(cutoffDatetime) =>
        db.run(
          deposits
            .filter(deposit =>
              deposit.portfolioId === portfolioId && deposit.depositDatetime > cutoffDatetime
            )
            .result
        )
      case None =>
        db.run(deposits.filter(_.portfolioId === portfolioId).result)
    }

  // TODO - this needs to be completely re-written now that cash is no longer an asset
  def create(deposit: Deposit): Future[Deposit] = {
    val insertDeposit: FixedSqlAction[Deposit, NoStream, Effect.Write] =
      deposits returning deposits.map(_.id) into ((record, id) =>
        deposit.copy(
          id = id,
          updatedAt = record.updatedAt,
          createdAt = record.createdAt
        )
      ) += deposit

    val getCashQuantity = portfolioAssets
      .filter(record => record.portfolioId === deposit.portfolioId)
      .map(_.quantity)
      .result
      .head

    def updateCashQuantity(quantity: BigDecimal) =
      portfolioAssets
        .filter(record =>
          record.portfolioId === deposit.portfolioId // && record.assetId == Cash.ASSET_ID
        )
        .map(_.quantity)
        .update(quantity)

    db.run((getCashQuantity flatMap { quantity =>
      updateCashQuantity(quantity)
    } andThen insertDeposit).transactionally)
  }
}

private[repositories] class DepositTable(tag: Tag)
    extends Table[Deposit](tag, "deposits")
    with AutoIncIdColumn
    with TimestampColumns {
  def portfolioId: Rep[Long] = column[Long]("portfolio_id")
  def totalAmount: Rep[BigMoney] = column[BigMoney]("total_amount")
  def depositDatetime: Rep[OffsetDateTime] = {
    column[OffsetDateTime]("deposit_datetime")
  }

  def * : ProvenShape[Deposit] =
    (
      id,
      portfolioId,
      totalAmount,
      depositDatetime,
      createdAt,
      updatedAt
    ) <> ((Deposit.apply _).tupled, Deposit.unapply)
}
