package controllers

import controllers.AuthorizationUtility.authorizedRequest
import models.db.Pack
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc.Request
import play.api.mvc.Results._
import play.api.test.FakeRequest
import play.api.test.Helpers.{await, _}
import services.db.{ClientPack, PacksDao}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PacksControllerTest extends WordSpecLike with MockFactory with BeforeAndAfter with Matchers {
  var controller: PacksController = _
  var dao: PacksDao = _

  before {
    dao = mock[PacksDao]
    val authorizedAction = new AuthorizedAction(AuthorizationUtility.config)
    controller = new PacksController(authorizedAction, dao)
  }

  "PacksController#allItems" should {
    "check authorization" in {
      status(controller.allItems().apply(FakeRequest())) shouldBe SEE_OTHER
    }

    "returns valid data" in {
      (dao.sorted _).expects().returns(Future(List(
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

      (dao.deleteBesides _).expects(List()).returns(Future(1))
      (dao.createItem _).expects(ClientPack(None, packName)).returns(Future(1))
      (dao.sorted _).expects().returns(Future(List()))

      await(controller.updateItems().apply(request)).header.status shouldEqual OK
    }

    "deletes items" in {
      val packId = 1

      (dao.deleteBesides _).expects(List(packId)).returns(Future(1))
      (dao.updateName _).expects(*, *).returns(Future(1))
      (dao.sorted _).expects().returns(Future(List()))

      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(packJson(packId, "test")))
      await(controller.updateItems().apply(request)).header.status shouldEqual OK
    }

    "updates items" in {
      val packId = 1
      val name = "test"

      (dao.deleteBesides _).expects(*).returns(Future(1))
      (dao.updateName _).expects(packId, name).returns(Future(1))
      (dao.sorted _).expects().returns(Future(List()))

      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(packJson(packId, name)))
      await(controller.updateItems().apply(request)).header.status shouldEqual OK
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
