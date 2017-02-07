package controllers

import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import play.api.mvc.Cookie
import play.api.mvc.Results._
import play.api.test.Helpers._
import play.api.test._
import services.ConfigurationService

class AuthorizationControllerTest extends WordSpecLike with Matchers with BeforeAndAfter with MockitoSugar {
  val login = "login"
  val password = "password"
  val salt = "$2a$10$lW.cT55o7ku2i/DIaHcGu."

  var controller: AuthorizationController = _
  var config: ConfigurationService = _

  before {
    config = mock[ConfigurationService]
    when(config.login).thenReturn(login)
    when(config.password).thenReturn(password)
    when(config.salt).thenReturn(salt)
    controller = new AuthorizationController(config)
  }

  "AuthorizationController#doLogin" should {
    "return bad request due to no form" in {
      await(controller.doLogin().apply(FakeRequest())) shouldEqual BadRequest
    }

    "does login" in {
      val request = FakeRequest().withFormUrlEncodedBody(
        "login" -> login,
        "password" -> password
      )
      val token = AuthorizedAction.createToken(config)
      val cookie = Cookie(AuthorizedAction.TOKEN_KEY, token, Some(AuthorizationController.SESSION_DURATION))
      await(controller.doLogin().apply(request)) shouldEqual Redirect(routes.Application.index("")).withCookies(cookie)
    }

    "return bad request due to incorrect login" in {
      val request = FakeRequest().withFormUrlEncodedBody(
        "login" -> "test",
        "password" -> password
      )
      await(controller.doLogin().apply(request)) shouldEqual BadRequest
    }

    "return bad request due to incorrect password" in {
      val request = FakeRequest().withFormUrlEncodedBody(
        "login" -> login,
        "password" -> "test"
      )
      await(controller.doLogin().apply(request)) shouldEqual BadRequest
    }

    "return bad request due to incorrect both credentials" in {
      val request = FakeRequest().withFormUrlEncodedBody(
        "login" -> "test",
        "password" -> "test"
      )
      await(controller.doLogin().apply(request)) shouldEqual BadRequest
    }
  }
}
