package persistence.repositories

import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import javax.inject.Singleton
import models.Withdrawal
import persistence.tables.WithdrawalTableDefinition

import java.time.OffsetDateTime
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
class WithdrawalRepository @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit
    ec: ExecutionContext
) extends BaseRepository
    with WithdrawalTableDefinition {
  import profile.api._

  def findById(id: Long): Future[Option[Withdrawal]] =
    db.run(withdrawals.filter(_.id === id).result.headOption)

  def findAllByPortfolioId(
      portfolioId: Long,
      since: Option[OffsetDateTime] = None
  ): Future[Seq[Withdrawal]] =
    since match {
      case Some(cutoffDatetime) =>
        db.run(
          withdrawals
            .filter(withdrawal =>
              withdrawal.portfolioId === portfolioId && withdrawal.withdrawalDatetime > cutoffDatetime
            )
            .result
        )
      case None =>
        db.run(withdrawals.filter(_.portfolioId === portfolioId).result)
    }

  def create(withdrawal: Withdrawal): Future[Withdrawal] = {
    val insertQuery =
      withdrawals returning withdrawals.map(_.id) into ((record, id) =>
        withdrawal.copy(
          id = id,
          updatedAt = record.updatedAt,
          createdAt = record.createdAt
        )
      )
    val action = insertQuery += withdrawal
    db.run(action)
  }
}
