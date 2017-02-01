package services

import org.mockito.ArgumentMatchers.{eq => Eq}
import org.mockito.Mockito.when
import org.mockito.{Mock, MockitoAnnotations}
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import play.api.Configuration

class ConfigurationServiceImplTest extends WordSpecLike with BeforeAndAfter with Matchers {
  @Mock
  var config: Configuration = _

  var service: ConfigurationServiceImpl = _

  before {
    MockitoAnnotations.initMocks(this)
    service = new ConfigurationServiceImpl(config)
  }

  "ConfigurationServiceImplTest" should {

    "get salt" in {
      val salt = "test salt"
      when(config.getString("stocks.salt")).thenReturn(Some(salt))
      service.salt shouldEqual salt
    }

    "get password" in {
      val password = "test password"
      when(config.getString("stocks.password")).thenReturn(Some(password))
      service.password shouldEqual password
    }

    "get login" in {
      val login = "test login"
      when(config.getString("stocks.login")).thenReturn(Some(login))
      service.login shouldEqual login
    }
  }
}
