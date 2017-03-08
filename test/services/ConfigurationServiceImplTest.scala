package services

import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import play.api.Configuration

class ConfigurationServiceImplTest extends WordSpecLike with BeforeAndAfter with Matchers with MockFactory {
  var config: Configuration = _

  var service: ConfigurationServiceImpl = _

  before {
    config = stub[Configuration]
    service = new ConfigurationServiceImpl(config)
  }

  "ConfigurationServiceImplTest" should {

    "get salt" in {
      val salt = "test salt"
      (config.getString _).when("stocks.salt", *).returns(Some(salt))
      service.salt shouldEqual salt
    }

    "get password" in {
      val password = "test password"
      (config.getString _).when("stocks.password", *).returns(Some(password))
      service.password shouldEqual password
    }

    "get login" in {
      val login = "test login"
      (config.getString _).when("stocks.login", *).returns(Some(login))
      service.login shouldEqual login
    }
  }
}
