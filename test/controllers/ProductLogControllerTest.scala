package controllers

import controllers.AuthorizationUtility.authorizedRequest
import models.db.{ProductLogAction, ProductLogEntry}
import org.joda.time.{DateTime, DateTimeZone}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import play.api.libs.json.Json
import play.api.mvc.Results.Ok
import play.api.test.FakeRequest
import play.api.test.Helpers.{await, _}
import services.db.ProductsLogsDao

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ProductLogControllerTest extends WordSpecLike with MockFactory with BeforeAndAfter with Matchers {
  var controller: ProductLogController = _
  var dao: ProductsLogsDao = _

  before {
    dao = mock[ProductsLogsDao]
    val authorizedAction = new AuthorizedAction(AuthorizationUtility.config)
    controller = new ProductLogController(authorizedAction, dao)
  }

  "ProductLogController#allLogs" should {
    "check authorization" in {
      status(controller.allLogs().apply(FakeRequest())) shouldBe SEE_OTHER
    }

    "returns valid data" in {
      val time = new DateTime(2017, 3, 5, 12, 18)

      (dao.allLogs _).expects().returns(Future(List(
        ProductLogEntry(1, 2, 3, 4, ProductLogAction.add, time)
      )))

      await(controller.allLogs.apply(authorizedRequest)) shouldEqual Ok(Json.arr(
        Json.obj(
          "id" -> 1,
          "productTypeId" -> 2,
          "categoryId" -> 3,
          "packId" -> 4,
          "action" -> "add",
          "time" -> time.withZone(DateTimeZone.UTC).getMillis
        )
      ))
    }
  }
}
