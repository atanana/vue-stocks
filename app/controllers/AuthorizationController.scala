package controllers

import javax.inject.Inject

import org.mindrot.jbcrypt.BCrypt
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import services.ConfigurationService

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
          Redirect(routes.Application.index()).withCookies(Cookie("token", createToken()))
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
