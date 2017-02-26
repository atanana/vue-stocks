package controllers

import org.mockito.Mockito.{mock, when}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AnyContentAsEmpty, Cookie}
import play.api.test.FakeRequest
import services.ConfigurationService

object AuthorizationUtility {
  def config: ConfigurationService = {
    val config = mock(classOf[ConfigurationService])
    when(config.login).thenReturn("login")
    when(config.password).thenReturn("password")
    when(config.salt).thenReturn("$2a$10$lW.cT55o7ku2i/DIaHcGu.")

    config
  }

  def authorizedRequest: FakeRequest[AnyContentAsEmpty.type] = {
    val token = AuthorizedAction.createToken(config)
    val cookie = Cookie(AuthorizedAction.TOKEN_KEY, token, Some(AuthorizationController.SESSION_DURATION))
    FakeRequest().withCookies(cookie)
  }

  def unauthorizedRequestWithJson: FakeRequest[JsValue] = FakeRequest().withBody[JsValue](Json.obj())
}
