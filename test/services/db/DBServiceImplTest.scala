package services.db

import org.mockito.Mockito.{verify, when}
import org.mockito.{Mock, MockitoAnnotations}
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import play.api.db.slick.DatabaseConfigProvider
import slick.backend.DatabaseConfig
import slick.dbio.{DBIOAction, NoStream}
import slick.driver.JdbcProfile
import slick.jdbc.JdbcBackend

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DBServiceImplTest extends WordSpecLike with BeforeAndAfter with Matchers {
  @Mock
  var dbProvider: DatabaseConfigProvider = _

  @Mock
  var db: JdbcBackend#DatabaseDef = _

  @Mock
  var dbConfig: DatabaseConfig[JdbcProfile] = _

  @Mock
  var action: DBIOAction[Any, NoStream, Nothing] = _

  var result: Future[String] = _

  var service: DBServiceImpl = _

  before {
    MockitoAnnotations.initMocks(this)

    when(dbProvider.get[JdbcProfile]).thenReturn(dbConfig)
    when(dbConfig.db).thenReturn(db)

    service = new DBServiceImpl(dbProvider)
    result = Future("123")
  }

  "DBServiceImplTest" should {

    "runAsync" in {
      when(db.run(action)).thenReturn(result)

      service.runAsync(action) shouldEqual result
      verify(db).run(action)
    }

    "run" in {
      when(db.run(action)).thenReturn(result)

      service.run(action) shouldEqual "123"
      verify(db).run(action)
    }

  }
}
