package controllers

import javax.inject.Inject

import models.db.{Categories, Category}
import play.api.libs.functional.syntax._
import play.api.libs.json.{Writes, _}
import services.db.DBService
import slick.driver.MySQLDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

case class ClientCategory(id: Option[Int], name: String) extends SimpleItem

class CategoriesController @Inject()(val db: DBService, val authorizedAction: AuthorizedAction) extends SimpleItemsHelper[ClientCategory, Category, Categories] {
  override protected implicit val table: TableQuery[Categories] = categories

  protected override def createItem(item: ClientCategory): Future[Int] = db.runAsync(categories += Category(0, item.name))

  protected implicit val itemReads: Reads[ClientCategory] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String]
    ) (ClientCategory.apply _)

  override protected implicit val itemWrites: Writes[Category] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String]
    ) (unlift(Category.unapply))
}
