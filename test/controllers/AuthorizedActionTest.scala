package controllers

import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import play.api.mvc.Cookie
import play.api.mvc.Results._
import play.api.test.Helpers._
import play.api.test._
import services.ConfigurationService

class AuthorizedActionTest extends WordSpecLike with BeforeAndAfter with MockitoSugar with Matchers {
  val login = "login"
  val salt = "$2a$10$lW.cT55o7ku2i/DIaHcGu."
  val password = "password"

  var action: AuthorizedAction = _
  var config: ConfigurationService = _

  before {
    config = mock[ConfigurationService]
    when(config.login).thenReturn(login)
    when(config.password).thenReturn(password)
    when(config.salt).thenReturn(salt)
    action = new AuthorizedAction(config)
  }

  "AuthorizedAction" should {
    "redirect to login" in {
      val future = action {
        Ok
      }.apply(FakeRequest())
      await(future) shouldEqual Redirect(routes.AuthorizationController.login())
    }

    "call underlying action" in {
      val result = Ok("successful result")
      val token = AuthorizedAction.createToken(config)
      val future = action {
        result
      }.apply(FakeRequest().withCookies(Cookie(AuthorizedAction.TOKEN_KEY, token, None)))
      await(future) shouldEqual result
    }
  }
}
