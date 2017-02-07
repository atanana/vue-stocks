package controllers

import org.mindrot.jbcrypt.BCrypt
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpecLike}
import services.ConfigurationService

class AuthorizedAction$Test extends WordSpecLike with Matchers with MockitoSugar {

  "AuthorizedAction$Test" should {

    "createToken" in {
      val service = mock[ConfigurationService]
      val login = "login"
      when(service.login).thenReturn(login)
      val password = "password"
      when(service.password).thenReturn(password)
      val salt = "$2a$10$lW.cT55o7ku2i/DIaHcGu."
      when(service.salt).thenReturn(salt)

      AuthorizedAction.createToken(service) shouldEqual BCrypt.hashpw(login + password, salt)
    }

  }
}
