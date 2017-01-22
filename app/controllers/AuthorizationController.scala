package controllers

import java.util.concurrent.TimeUnit
import javax.inject.Inject

import org.mindrot.jbcrypt.BCrypt
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import services.ConfigurationService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration

case class UserData(login: String, password: String)

class AuthorizationController @Inject()(val config: ConfigurationService) extends Controller {
  private val userForm = Form(
    mapping(
      "login" -> text,
      "password" -> text
    )(UserData.apply)(UserData.unapply)
  )

  def login = Action {
    Ok(views.html.login())
  }

  def doLogin() = Action { implicit request =>
    userForm.bindFromRequest().fold(
      _ => BadRequest,
      user => {
        if (UserData(config.login, config.password) == user) {
          val duration = Duration(30, TimeUnit.DAYS).toSeconds.toInt
          Redirect(routes.Application.index()).withCookies(Cookie(AuthorizedAction.TOKEN_KEY, createToken(), Some(duration)))
        } else {
          BadRequest
        }
      }
    )
  }

  private def createToken() = {
    BCrypt.hashpw(config.login + config.password, config.salt)
  }
}

object AuthorizedAction extends ActionBuilder[Request] with Results {
  val TOKEN_KEY = "token"

  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
    val token = request.cookies.get(TOKEN_KEY).map(_.value)
    if (token.isDefined) {
      block(request)
    } else {
      Future(Redirect(routes.AuthorizationController.login()))
    }
  }
}