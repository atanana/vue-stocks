package controllers

import controllers.AuthorizationUtility.authorizedRequest
import models.db.Category
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import play.api.http.Status.SEE_OTHER
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc.Request
import play.api.mvc.Results._
import play.api.test.FakeRequest
import play.api.test.Helpers.{await, _}
import services.db.{CategoriesDao, ClientCategory}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CategoriesControllerTest extends WordSpecLike with MockFactory with BeforeAndAfter with Matchers {
  var controller: CategoriesController = _
  var dao: CategoriesDao = _

  before {
    dao = mock[CategoriesDao]
    val authorizedAction = new AuthorizedAction(AuthorizationUtility.config)
    controller = new CategoriesController(authorizedAction, dao)
  }

  "CategoriesController#allItems" should {
    "check authorization" in {
      status(controller.allItems().apply(FakeRequest())) shouldBe SEE_OTHER
    }

    "returns valid data" in {
      (dao.sorted _).expects().returns(Future(List(
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
      status(controller.updateItems().apply(AuthorizationUtility.unauthorizedRequestWithJson)) shouldBe SEE_OTHER
    }

    "return bad request due to incorrect json" in {
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.obj())
      await(controller.updateItems().apply(request)) shouldEqual BadRequest
    }

    "returns all items" in {
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr())
      (dao.deleteBesides _).expects(*).returns(Future(1))
      (dao.sorted _).expects().returns(Future(List(
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
      (dao.deleteBesides _).expects(*).returns(Future(1))
      (dao.createItem _).expects(ClientCategory(None, categoryName)).returns(Future(1))
      (dao.sorted _).expects().returns(Future(List()))

      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(newCategoryJson(categoryName)))
      await(controller.updateItems().apply(request)).header.status shouldEqual OK
    }

    "deletes items" in {
      val categoryId = 1
      (dao.deleteBesides _).expects(List(categoryId)).returns(Future(1))
      (dao.updateName _).expects(*, *).returns(Future(1))
      (dao.sorted _).expects().returns(Future(List()))

      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(categoryJson(categoryId, "test")))
      await(controller.updateItems().apply(request)).header.status shouldEqual OK
    }

    "updates items" in {
      val categoryId = 1
      val categoryName = "test"
      (dao.deleteBesides _).expects(List(categoryId)).returns(Future(1))
      (dao.updateName _).expects(categoryId, categoryName).returns(Future(1))
      (dao.sorted _).expects().returns(Future(List()))

      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(categoryJson(categoryId, categoryName)))
      await(controller.updateItems().apply(request)).header.status shouldEqual OK
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
