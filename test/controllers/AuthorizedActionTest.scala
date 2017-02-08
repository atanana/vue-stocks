package controllers

import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import play.api.mvc.Results._
import play.api.test.Helpers._
import play.api.test._
import services.ConfigurationService

class AuthorizedActionTest extends WordSpecLike with BeforeAndAfter with MockitoSugar with Matchers {
  val config: ConfigurationService = AuthorizationUtility.config
  var action: AuthorizedAction = _

  before {
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
      val future = action {
        result
      }.apply(AuthorizationUtility.authorizedRequest)
      await(future) shouldEqual result
    }
  }
}
