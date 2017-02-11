package controllers

import controllers.AuthorizationUtility.authorizedRequest
import models.db.Category
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{spy, verify, when}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc.BodyParsers.parse
import play.api.mvc.Request
import play.api.mvc.Results._
import play.api.test.FakeRequest
import play.api.test.Helpers.{await, _}
import services.db.{CategoriesDao, ClientCategory}

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

    when(dao.deleteBesides(any())).thenReturn(Future(1))
    when(dao.updateName(any(), any())).thenReturn(Future(1))
    when(dao.createItem(any())).thenReturn(Future(1))
    when(dao.sorted()).thenReturn(Future(List()))
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

  "CategoriesController#updateItems" should {
    "check authorization" in {
      controller.updateItems().apply(FakeRequest())
      verify(authorizedAction).async(parse.json)(any())
    }

    "return bad request due to incorrect json" in {
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.obj())
      await(controller.updateItems().apply(request)) shouldEqual BadRequest
    }

    "returns all items" in {
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr())
      when(dao.sorted()).thenReturn(Future(List(
        Category(1, "test 1"),
        Category(2, "test 2"),
        Category(3, "test 3")
      )))
      await(controller.updateItems().apply(request)) shouldEqual Ok(Json.arr(
        categoryJson(1, "test 1"),
        categoryJson(2, "test 2"),
        categoryJson(3, "test 3")
      ))
    }

    "creates new items" in {
      val categoryName = "test 1"
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(newCategoryJson(categoryName)))
      await(controller.updateItems().apply(request)).header.status shouldEqual OK
      verify(dao).createItem(ClientCategory(None, categoryName))
    }

    "deletes items" in {
      val categoryId = 1
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(categoryJson(categoryId, "test")))
      await(controller.updateItems().apply(request)).header.status shouldEqual OK
      verify(dao).deleteBesides(List(categoryId))
    }

    "updates items" in {
      val categoryId = 1
      val categoryName = "test"
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(categoryJson(categoryId, categoryName)))
      await(controller.updateItems().apply(request)).header.status shouldEqual OK
      verify(dao).updateName(categoryId, categoryName)
    }
  }

  private def newCategoryJson(name: String): JsObject = {
    Json.obj(
      "name" -> name
    )
  }

  private def categoryJson(id: Int, name: String): JsObject = {
    Json.obj(
      "id" -> id,
      "name" -> name
    )
  }
}
