package controllers

import controllers.AuthorizationUtility.authorizedRequest
import models.db.ProductType
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{spy, verify, when}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import play.api.libs.json.{JsNumber, JsObject, JsValue, Json}
import play.api.mvc.BodyParsers.parse
import play.api.mvc.Request
import play.api.mvc.Results._
import play.api.test.FakeRequest
import play.api.test.Helpers.{await, _}
import services.db.{ClientProductType, ProductTypesDao}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ProductTypesControllerTest extends WordSpecLike with MockitoSugar with BeforeAndAfter with Matchers {
  var controller: ProductTypesController = _
  var authorizedAction: AuthorizedAction = _
  var dao: ProductTypesDao = _

  before {
    dao = mock[ProductTypesDao]
    authorizedAction = spy(new AuthorizedAction(AuthorizationUtility.config))
    controller = new ProductTypesController(authorizedAction, dao)

    when(dao.deleteBesides(any())).thenReturn(Future(1))
    when(dao.updateProductType(any(), any())).thenReturn(Future(1))
    when(dao.createItem(any())).thenReturn(Future(1))
    when(dao.sorted()).thenReturn(Future(List()))
  }

  "PacksController#allItems" should {
    "check authorization" in {
      controller.allItems().apply(FakeRequest())
      verify(authorizedAction).async(parse.json)(any())
    }

    "returns valid data" in {
      when(dao.sorted()).thenReturn(Future(List(
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
      await(controller.updateItems().apply(request)).header.status shouldEqual OK
      verify(dao).createItem(ClientProductType(None, packName1, Some(categoryId)))
      verify(dao).createItem(ClientProductType(None, packName2, None))
    }

    "deletes items" in {
      val categoryId = 1
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(productTypeJson(categoryId, "test", None)))
      await(controller.updateItems().apply(request)).header.status shouldEqual OK
      verify(dao).deleteBesides(List(categoryId))
    }

    "updates items" in {
      val packId1 = 1
      val packId2 = 1
      val packName1 = "test 1"
      val packName2 = "test 2"
      val categoryId2 = 2
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(
        productTypeJson(packId1, packName1, None),
        productTypeJson(packId2, packName2, Some(categoryId2))
      ))
      await(controller.updateItems().apply(request)).header.status shouldEqual OK
      verify(dao).updateProductType(ClientProductType(Some(packId1), packName1, None), packId1)
      verify(dao).updateProductType(ClientProductType(Some(packId2), packName2, Some(categoryId2)), packId2)
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
