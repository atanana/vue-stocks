package controllers

import controllers.AuthorizationUtility.authorizedRequest
import models.db.Category
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{spy, verify, when}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.BodyParsers.parse
import play.api.mvc.Results._
import play.api.test.FakeRequest
import play.api.test.Helpers.{await, _}
import services.db.CategoriesDao

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CategoriesControllerTest extends WordSpecLike with MockitoSugar with BeforeAndAfter with Matchers {
  var controller: CategoriesController = _
  var authorizedAction: AuthorizedAction = _
  var dao: CategoriesDao = _

  before {
    dao = mock[CategoriesDao]
    authorizedAction = spy(new AuthorizedAction(AuthorizationUtility.config))
    controller = new CategoriesController(authorizedAction, dao)
  }

  "CategoriesController#allItems" should {
    "check authorization" in {
      controller.allItems().apply(FakeRequest())
      verify(authorizedAction).async(parse.json)(any())
    }

    "returns valid data" in {
      when(dao.sorted()).thenReturn(Future(List(
        Category(1, "test 1"),
        Category(2, "test 2"),
        Category(3, "test 3")
      )))
      await(controller.allItems.apply(authorizedRequest)) shouldEqual Ok(Json.arr(
        categoryJson(1, "test 1"),
        categoryJson(2, "test 2"),
        categoryJson(3, "test 3")
      ))
    }
  }

  private def categoryJson(id: Int, name: String): JsObject = {
    Json.obj(
      "id" -> id,
      "name" -> name
    )
  }
}
