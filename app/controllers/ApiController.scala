package controllers

import play.api.mvc.BaseController
import play.api.mvc.ControllerComponents
import persistence.repositories.DepositRepository
import persistence.repositories.PortfolioRepository

import javax.inject.Inject
import javax.inject.Singleton
import scala.concurrent.ExecutionContext

@Singleton
class ApiController @Inject() (
    val controllerComponents: ControllerComponents,
    val portfolioRepo: PortfolioRepository,
    val depositRepo: DepositRepository
)(implicit ec: ExecutionContext)
    extends BaseController {
  // TODO
}
