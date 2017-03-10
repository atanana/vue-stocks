package controllers

import controllers.AuthorizationUtility.authorizedRequest
import models.db.MenuItem
import org.joda.time.{DateTimeZone, LocalDate}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc.Request
import play.api.mvc.Results.{Ok, _}
import play.api.test.FakeRequest
import play.api.test.Helpers.{await, status, _}
import services.db.{ClientMenuItem, MenuItemsDao}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MenuItemsControllerTest extends WordSpecLike with MockFactory with BeforeAndAfter with Matchers {
  var controller: MenuItemsController = _
  var dao: MenuItemsDao = _

  before {
    dao = mock[MenuItemsDao]
    val authorizedAction = new AuthorizedAction(AuthorizationUtility.config)
    controller = new MenuItemsController(authorizedAction, dao)
  }

  "MenuItemsController#allItems" should {
    "check authorization" in {
      status(controller.allItems().apply(FakeRequest())) shouldBe SEE_OTHER
    }

    "returns valid data" in {
      val item1 = MenuItem(1, "test 1", new LocalDate(2017, 3, 10))
      val item2 = MenuItem(2, "test 2", new LocalDate(2017, 3, 11))
      val item3 = MenuItem(3, "test 3", new LocalDate(2017, 3, 12))
      (dao.sorted _).expects().returns(Future(List(item1, item2, item3)))

      await(controller.allItems.apply(authorizedRequest)) shouldEqual Ok(Json.arr(
        menuItemJson(item1),
        menuItemJson(item2),
        menuItemJson(item3)
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
      (dao.deleteBesides _).expects(*).returns(Future(1))
      val item1 = MenuItem(1, "test 1", new LocalDate(2017, 3, 10))
      val item2 = MenuItem(2, "test 2", new LocalDate(2017, 3, 11))
      val item3 = MenuItem(3, "test 3", new LocalDate(2017, 3, 12))
      (dao.sorted _).expects().returns(Future(List(item1, item2, item3)))

      await(controller.updateItems().apply(request)) shouldEqual Ok(Json.arr(
        menuItemJson(item1),
        menuItemJson(item2),
        menuItemJson(item3)
      ))
    }

    "creates new items" in {
      val itemName = "test 1"
      val date = new LocalDate(2017, 3, 10)
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(
        newMenuItemJson(itemName, date)
      ))

      (dao.deleteBesides _).expects(*).returns(Future(1))
      (dao.createItem _).expects(ClientMenuItem(None, itemName, date)).returns(Future(1))
      (dao.sorted _).expects().returns(Future(List()))

      await(controller.updateItems().apply(request)).header.status shouldEqual OK
    }

    "deletes items" in {
      val item = MenuItem(1, "test 1", new LocalDate(2017, 3, 10))
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(menuItemJson(item)))

      (dao.deleteBesides _).expects(List(item.id)).returns(Future(1))
      (dao.updateMenuItem _).expects(*, *).returns(Future(1))
      (dao.sorted _).expects().returns(Future(List()))

      await(controller.updateItems().apply(request)).header.status shouldEqual OK
    }

    "updates items" in {
      val item = MenuItem(1, "test 1", new LocalDate(2017, 3, 10))
      val request: Request[JsValue] = authorizedRequest.withBody[JsValue](Json.arr(menuItemJson(item)))

      (dao.deleteBesides _).expects(*).returns(Future(1))
      (dao.updateMenuItem _).expects(ClientMenuItem(Some(item.id), item.name, item.date), item.id).returns(Future(1))
      (dao.sorted _).expects().returns(Future(List()))

      await(controller.updateItems().apply(request)).header.status shouldEqual OK
    }
  }

  private def newMenuItemJson(name: String, date: LocalDate): JsObject = {
    Json.obj(
      "name" -> name,
      "date" -> date.toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis
    )
  }

  private def menuItemJson(menuItem: MenuItem): JsObject = {
    Json.obj(
      "id" -> menuItem.id,
      "name" -> menuItem.name,
      "date" -> menuItem.date.toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis
    )
  }
}
