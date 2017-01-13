package controllers

import javax.inject.Inject

import models.db.{Categories, Category}
import play.api.libs.functional.syntax._
import play.api.libs.json.{Json, Writes, _}
import play.api.mvc._
import services.db.DBService
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class ClientCategory(id: Option[Int], name: String) extends SimpleItem

class CategoriesController @Inject()(val db: DBService) extends SimpleItemsHelper[ClientCategory, Category, Categories] {
  implicit val categoryWrites: Writes[Category] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String]
    ) (unlift(Category.unapply))

  protected override def create(name: String): Future[Int] = addCategory(name)

  protected override def sortedItems(): Future[Result] = {
    sorted[Category, Categories].map(categories => {
      Ok(Json.toJson(categories))
    })
  }

  protected implicit val itemReads: Reads[ClientCategory] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String]
    ) (ClientCategory.apply _)

  override protected implicit val table: TableQuery[Categories] = categories
}
