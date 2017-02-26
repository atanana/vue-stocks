package controllers

import controllers.AuthorizationUtility.authorizedRequest
import models.db.Product
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import play.api.libs.json._
import play.api.mvc.Request
import play.api.mvc.Results._
import play.api.test.Helpers._
import play.api.test._
import services.db.{ClientProduct, ProductsDao}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ProductsControllerTest extends WordSpecLike with MockFactory with BeforeAndAfter with Matchers {
  var controller: ProductsController = _
  var dao: ProductsDao = _

  before {
    dao = mock[ProductsDao]
    val authorizedAction = new AuthorizedAction(AuthorizationUtility.config)
    controller = new ProductsController(authorizedAction, dao)
  }

  "ProductsController#addProduct" should {
    "check authorization" in {
      status(controller.addProduct().apply(AuthorizationUtility.unauthorizedRequestWithJson)) shouldBe SEE_OTHER
    }

    "return bad request due to no json" in {
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](JsArray())
      await(controller.addProduct().apply(request)) shouldEqual BadRequest
    }

    "add product and return all products" in {
      val categoryId = 12
      val productTypeId = 123
      val packId = 1234
      val payload = Json.obj(
        "categoryId" -> categoryId,
        "productTypeId" -> productTypeId,
        "packId" -> packId
      )
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](payload)
      (dao.allProducts _).expects().returns(Future(List()))
      (dao.addProduct _).expects(ClientProduct(categoryId, productTypeId, packId)).returns(Future(1))

      await(controller.addProduct().apply(request)) shouldEqual Ok(JsArray())
    }
  }

  "ProductsController#deleteProduct" should {
    "check authorization" in {
      status(controller.deleteProduct().apply(AuthorizationUtility.unauthorizedRequestWithJson)) shouldBe SEE_OTHER
    }

    "return bad request due to no json" in {
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](JsArray())
      await(controller.deleteProduct().apply(request)) shouldEqual BadRequest
    }

    "delete product and return all products" in {
      val categoryId = 12
      val productTypeId = 123
      val packId = 1234
      val payload = Json.obj(
        "categoryId" -> categoryId,
        "productTypeId" -> productTypeId,
        "packId" -> packId
      )
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](payload)
      (dao.allProducts _).expects().returns(Future(List()))
      (dao.deleteProduct _).expects(ClientProduct(categoryId, productTypeId, packId)).returns(Future(1))

      await(controller.deleteProduct().apply(request)) shouldEqual Ok(JsArray())
    }
  }

  "ProductsController#allProducts" should {
    "check authorization" in {
      status(controller.allProducts().apply(FakeRequest())) shouldBe SEE_OTHER
    }

    "correct group products" in {
      (dao.allProducts _).expects().returns(Future(List(
        Product(1, 1, 1, 1),
        Product(2, 1, 2, 1),
        Product(3, 2, 2, 2),
        Product(4, 3, 3, 3),
        Product(5, 3, 1, 3)
      )))

      val result = await(controller.allProducts.apply(authorizedRequest))
      result.header.status shouldEqual OK
      contentAsJson(Future(result)).as[JsArray].value.toSet shouldEqual Set(
        productJson(1, 1, 1, 1),
        productJson(1, 2, 1, 1),
        productJson(2, 2, 2, 1),
        productJson(3, 3, 3, 1),
        productJson(3, 1, 3, 1)
      )
    }

    "correct group product packs" in {
      (dao.allProducts _).expects().returns(Future(List(
        Product(1, 1, 1, 1),
        Product(2, 1, 1, 2),
        Product(3, 1, 1, 1)
      )))

      val result = await(controller.allProducts.apply(authorizedRequest))
      result.header.status shouldEqual OK
      val product = contentAsJson(Future(result)).as[JsArray].value.head.as[JsObject].value
      product("productTypeId") shouldEqual JsNumber(1)
      product("categoryId") shouldEqual JsNumber(1)
      product("packs").as[JsArray].value.toSet shouldEqual Set(
        packJson(1, 2),
        packJson(2, 1)
      )
    }
  }

  private def productJson(productTypeId: Int, categoryId: Int, packId: Int, quantity: Int): JsObject = {
    Json.obj(
      "productTypeId" -> productTypeId,
      "categoryId" -> categoryId,
      "packs" -> Json.arr(packJson(packId, quantity))
    )
  }

  private def packJson(packId: Int, quantity: Int) = {
    Json.obj(
      "packId" -> packId,
      "quantity" -> quantity
    )
  }
}
