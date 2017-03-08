package controllers

import org.scalamock.scalatest.MockFactory
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AnyContentAsEmpty, Cookie}
import play.api.test.FakeRequest
import services.ConfigurationService

object AuthorizationUtility extends MockFactory {
  def config: ConfigurationService = {
    val config = stub[ConfigurationService]
    (config.login _).when().returns("login")
    (config.password _).when().returns("password")
    (config.salt _).when().returns("$2a$10$lW.cT55o7ku2i/DIaHcGu.")

    config
  }

  def authorizedRequest: FakeRequest[AnyContentAsEmpty.type] = {
    val token = AuthorizedAction.createToken(config)
    val cookie = Cookie(AuthorizedAction.TOKEN_KEY, token, Some(AuthorizationController.SESSION_DURATION))
    FakeRequest().withCookies(cookie)
  }

  def unauthorizedRequestWithJson: FakeRequest[JsValue] = FakeRequest().withBody[JsValue](Json.obj())
}
