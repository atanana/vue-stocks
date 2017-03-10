package controllers

import javax.inject.Inject

import models.db.{MenuItem, MenuItems}
import org.joda.time.{DateTime, DateTimeZone, LocalDate}
import play.api.libs.functional.syntax.{unlift, _}
import play.api.libs.json._
import services.db.{ClientMenuItem, MenuItemsDao}

import scala.concurrent.Future

class MenuItemsController @Inject()(val authorizedAction: AuthorizedAction, menuDao: MenuItemsDao)
  extends SimpleItemsHelper[ClientMenuItem, MenuItem, MenuItems](menuDao) {

  private implicit val localDateReads: Reads[LocalDate] = Reads.apply(json =>
    json.asOpt[JsNumber]
      .map(_.value.longValue())
      .map(new DateTime(_, DateTimeZone.UTC).toLocalDate)
      .map(JsSuccess(_))
      .getOrElse(JsError())
  )
  private implicit val localDateWrites: Writes[LocalDate] = Writes.apply(localDate => JsNumber(localDate.toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis))

  protected implicit val itemReads: Reads[ClientMenuItem] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "date").read[LocalDate]
    ) (ClientMenuItem.apply _)

  override protected implicit val itemWrites: Writes[MenuItem] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String] and
      (JsPath \ "date").write[LocalDate]
    ) (unlift(MenuItem.unapply))

  override protected def updateItem(item: ClientMenuItem, id: Int): Future[Int] = menuDao.updateMenuItem(item, id)
}
