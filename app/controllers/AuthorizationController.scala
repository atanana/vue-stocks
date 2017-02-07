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
          val token = AuthorizedAction.createToken(config)
          val cookie = Cookie(AuthorizedAction.TOKEN_KEY, token, Some(AuthorizationController.SESSION_DURATION))
          Redirect(routes.Application.index("")).withCookies(cookie)
        } else {
          BadRequest
        }
      }
    )
  }
}

object AuthorizationController {
  val SESSION_DURATION: Int = Duration(30, TimeUnit.DAYS).toSeconds.toInt
}

class AuthorizedAction @Inject()(val config: ConfigurationService) extends ActionBuilder[Request] with Results {
  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
    if (request.cookies.get(AuthorizedAction.TOKEN_KEY)
      .map(_.value)
      .contains(AuthorizedAction.createToken(config))) {
      block(request)
    } else {
      Future(Redirect(routes.AuthorizationController.login()))
    }
  }
}

object AuthorizedAction {
  val TOKEN_KEY = "token"

  def createToken(config: ConfigurationService): String = {
    BCrypt.hashpw(config.login + config.password, config.salt)
  }
}