package controllers

import controllers.AuthorizationUtility.authorizedRequest
import models.db.Pack
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
import services.db.{ClientPack, PacksDao}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PacksControllerTest extends WordSpecLike with MockitoSugar with BeforeAndAfter with Matchers {
  var controller: PacksController = _
  var authorizedAction: AuthorizedAction = _
  var dao: PacksDao = _

  before {
    dao = mock[PacksDao]
    authorizedAction = spy(new AuthorizedAction(AuthorizationUtility.config))
    controller = new PacksController(authorizedAction, dao)

    when(dao.deleteBesides(any())).thenReturn(Future(1))
    when(dao.updateName(any(), any())).thenReturn(Future(1))
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
        Pack(1, "test 1"),
        Pack(2, "test 2"),
        Pack(3, "test 3")
      )))
      await(controller.allItems.apply(authorizedRequest)) shouldEqual Ok(Json.arr(
        packJson(1, "test 1"),
        packJson(2, "test 2"),
        packJson(3, "test 3")
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
        Pack(1, "test 1"),
        Pack(2, "test 2"),
        Pack(3, "test 3")
      )))
      await(controller.updateItems().apply(request)) shouldEqual Ok(Json.arr(
        packJson(1, "test 1"),
        packJson(2, "test 2"),
        packJson(3, "test 3")
      ))
    }

    "creates new items" in {
      val packName = "test 1"
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(newPackJson(packName)))
      await(controller.updateItems().apply(request)).header.status shouldEqual OK
      verify(dao).createItem(ClientPack(None, packName))
    }

    "deletes items" in {
      val categoryId = 1
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(packJson(categoryId, "test")))
      await(controller.updateItems().apply(request)).header.status shouldEqual OK
      verify(dao).deleteBesides(List(categoryId))
    }

    "updates items" in {
      val packId = 1
      val packName = "test"
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(packJson(packId, packName)))
      await(controller.updateItems().apply(request)).header.status shouldEqual OK
      verify(dao).updateName(packId, packName)
    }
  }

  private def newPackJson(name: String): JsObject = {
    Json.obj(
      "name" -> name
    )
  }

  private def packJson(id: Int, name: String): JsObject = {
    Json.obj(
      "id" -> id,
      "name" -> name
    )
  }
}
