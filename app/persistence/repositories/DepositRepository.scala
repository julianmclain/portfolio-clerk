package persistence.repositories

import models.Deposit
import persistence.ApplicationPostgresProfile
import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import play.api.db.slick.HasDatabaseConfigProvider
import persistence.tables.DepositTableDefinition

import java.time.OffsetDateTime

@Singleton
class DepositRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends HasDatabaseConfigProvider[ApplicationPostgresProfile]
    with DepositTableDefinition {
  import profile.api._

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

  def create(deposit: Deposit): Future[Deposit] = {
    // TODO - this needs to be completely re-written now that cash is no longer an asset
    // it used to have this type signature, leaving in case i broke something...
    // val insertDeposit: FixedSqlAction[Deposit, NoStream, Effect.Write] =
    val insertDeposit =
      deposits returning deposits.map(_.id) into ((record, id) =>
        deposit.copy(
          id = id,
          updatedAt = record.updatedAt,
          createdAt = record.createdAt
        )
      ) += deposit
    db.run(insertDeposit)
  }
}
