package controllers

import controllers.AuthorizationUtility.authorizedRequest
import models.db.Product
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{spy, verify, when}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import play.api.libs.json._
import play.api.mvc.BodyParsers.parse
import play.api.mvc.Request
import play.api.mvc.Results._
import play.api.test.Helpers._
import play.api.test._
import services.db.{ClientProduct, DBService, ProductsDao}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ProductsControllerTest extends WordSpecLike with MockitoSugar with BeforeAndAfter with Matchers {
  var controller: ProductsController = _
  var db: DBService = _
  var authorizedAction: AuthorizedAction = _
  var productsDao: ProductsDao = _

  before {
    db = mock[DBService]
    productsDao = mock[ProductsDao]
    authorizedAction = spy(new AuthorizedAction(AuthorizationUtility.config))
    controller = new ProductsController(db, authorizedAction, productsDao)
  }

  "ProductsController#addProduct" should {
    "check authorization" in {
      controller.addProduct().apply(FakeRequest())
      verify(authorizedAction).async(parse.json)(any())
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
      when(productsDao.allProducts).thenReturn(Future(List()))
      when(productsDao.addProduct(any())).thenReturn(Future(1))

      await(controller.addProduct().apply(request)) shouldEqual Ok(JsArray())

      verify(productsDao).addProduct(ClientProduct(categoryId, productTypeId, packId))
      verify(productsDao).allProducts
    }
  }

  "ProductsController#deleteProduct" should {
    "check authorization" in {
      controller.deleteProduct().apply(FakeRequest())
      verify(authorizedAction).async(parse.json)(any())
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
      when(productsDao.allProducts).thenReturn(Future(List()))
      when(productsDao.deleteProduct(any())).thenReturn(Future(1))

      await(controller.deleteProduct().apply(request)) shouldEqual Ok(JsArray())

      verify(productsDao).deleteProduct(ClientProduct(categoryId, productTypeId, packId))
      verify(productsDao).allProducts
    }
  }

  "ProductsController#allProducts" should {
    "check authorization" in {
      controller.allProducts().apply(FakeRequest())
      verify(authorizedAction).async(parse.json)(any())
    }

    "correct group products" in {
      when(productsDao.allProducts).thenReturn(Future(List(
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
      when(productsDao.allProducts).thenReturn(Future(List(
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
