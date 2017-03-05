package controllers

import javax.inject.Inject

import models.db.ProductLogAction.ProductLogAction
import models.db.ProductLogEntry
import org.joda.time.DateTime
import play.api.libs.functional.syntax.{unlift, _}
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, Controller}
import services.db.ProductsLogsDao

import scala.concurrent.ExecutionContext.Implicits.global


class ProductLogController @Inject()(authorizedAction: AuthorizedAction, val logsDao: ProductsLogsDao) extends Controller {
  private implicit val dateTimeWrites: Writes[DateTime] = Writes.apply(dateTime => JsNumber(dateTime.getMillis))

  private implicit val productsLogsWrites: Writes[ProductLogEntry] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "productTypeId").write[Int] and
      (JsPath \ "categoryId").write[Int] and
      (JsPath \ "packId").write[Int] and
      (JsPath \ "action").write[ProductLogAction] and
      (JsPath \ "time").write[DateTime]
    ) (unlift(ProductLogEntry.unapply))

  def allLogs: Action[AnyContent] = authorizedAction.async {
    logsDao.allLogs.map(logs => Ok(Json.toJson(logs)))
  }
}
