package controllers

import controllers.AuthorizationUtility.authorizedRequest
import models.db.ProductType
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import play.api.libs.json.{JsNumber, JsObject, JsValue, Json}
import play.api.mvc.Request
import play.api.mvc.Results._
import play.api.test.FakeRequest
import play.api.test.Helpers.{await, _}
import services.db.{ClientProductType, ProductTypesDao}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ProductTypesControllerTest extends WordSpecLike with MockFactory with BeforeAndAfter with Matchers {
  var controller: ProductTypesController = _
  var dao: ProductTypesDao = _

  before {
    dao = mock[ProductTypesDao]
    val authorizedAction = new AuthorizedAction(AuthorizationUtility.config)
    controller = new ProductTypesController(authorizedAction, dao)
  }

  "PacksController#allItems" should {
    "check authorization" in {
      status(controller.allItems().apply(FakeRequest())) shouldBe SEE_OTHER
    }

    "returns valid data" in {
      (dao.sorted _).expects().returns(Future(List(
        ProductType(1, "test 1", Some(1)),
        ProductType(2, "test 2", None),
        ProductType(3, "test 3", Some(3))
      )))

      await(controller.allItems.apply(authorizedRequest)) shouldEqual Ok(Json.arr(
        productTypeJson(1, "test 1", Some(1)),
        productTypeJson(2, "test 2", None),
        productTypeJson(3, "test 3", Some(3))
      ))
    }
  }

  "PacksController#updateItems" should {
    "check authorization" in {
      status(controller.updateItems().apply(AuthorizationUtility.unauthorizedRequestWithJson)) shouldBe SEE_OTHER
    }

    "return bad request due to incorrect json" in {
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.obj())
      await(controller.updateItems().apply(request)) shouldEqual BadRequest
    }

    "returns all items" in {
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr())
      (dao.deleteBesides _).expects(List()).returns(Future(1))
      (dao.sorted _).expects().returns(Future(List(
        ProductType(1, "test 1", Some(1)),
        ProductType(2, "test 2", None),
        ProductType(3, "test 3", Some(3))
      )))

      await(controller.updateItems().apply(request)) shouldEqual Ok(Json.arr(
        productTypeJson(1, "test 1", Some(1)),
        productTypeJson(2, "test 2", None),
        productTypeJson(3, "test 3", Some(3))
      ))
    }

    "creates new items" in {
      val packName1 = "test 1"
      val packName2 = "test 2"
      val categoryId = 1
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(
        newProductTypeJson(packName1, Some(categoryId)),
        newProductTypeJson(packName2, None)
      ))

      (dao.deleteBesides _).expects(List()).returns(Future(1))
      (dao.createItem _).expects(ClientProductType(None, packName1, Some(categoryId))).returns(Future(1))
      (dao.createItem _).expects(ClientProductType(None, packName2, None)).returns(Future(1))
      (dao.sorted _).expects().returns(Future(List()))

      await(controller.updateItems().apply(request)).header.status shouldEqual OK
    }

    "deletes items" in {
      val productTypeId = 1
      val name = "test"
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(productTypeJson(productTypeId, name, None)))

      (dao.deleteBesides _).expects(List(productTypeId)).returns(Future(1))
      (dao.updateProductType _).expects(ClientProductType(Some(productTypeId), name, None), productTypeId).returns(Future(1))
      (dao.sorted _).expects().returns(Future(List()))

      await(controller.updateItems().apply(request)).header.status shouldEqual OK
    }

    "updates items" in {
      val packId1 = 1
      val packId2 = 2
      val packName1 = "test 1"
      val packName2 = "test 2"
      val categoryId2 = 2
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(
        productTypeJson(packId1, packName1, None),
        productTypeJson(packId2, packName2, Some(categoryId2))
      ))

      (dao.deleteBesides _).expects(List(packId1, packId2)).returns(Future(1))
      (dao.updateProductType _).expects(ClientProductType(Some(packId1), packName1, None), packId1).returns(Future(1))
      (dao.updateProductType _).expects(ClientProductType(Some(packId2), packName2, Some(categoryId2)), packId2).returns(Future(1))
      (dao.sorted _).expects().returns(Future(List()))

      await(controller.updateItems().apply(request)).header.status shouldEqual OK
    }
  }

  private def newProductTypeJson(name: String, categoryIdOption: Option[Int]): JsObject = {
    var result = Json.obj(
      "name" -> name
    )
    categoryIdOption.foreach(categoryId => result = result + ("categoryId" -> JsNumber(categoryId)))
    result
  }

  private def productTypeJson(id: Int, name: String, categoryIdOption: Option[Int]): JsObject = {
    var result = Json.obj(
      "id" -> id,
      "name" -> name
    )
    categoryIdOption.foreach(categoryId => result = result + ("categoryId" -> JsNumber(categoryId)))
    result
  }
}
