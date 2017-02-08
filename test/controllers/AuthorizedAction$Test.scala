package controllers

import org.mindrot.jbcrypt.BCrypt
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpecLike}

class AuthorizedAction$Test extends WordSpecLike with Matchers with MockitoSugar {

  "AuthorizedAction$Test" should {

    "createToken" in {
      val config = AuthorizationUtility.config
      AuthorizedAction.createToken(config) shouldEqual BCrypt.hashpw(config.login + config.password, config.salt)
    }

  }
}
