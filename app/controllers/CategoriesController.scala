package controllers

import javax.inject.Inject

import models.db.{Categories, Category, Tables}
import play.api.libs.functional.syntax._
import play.api.libs.json.{Json, Writes, _}
import play.api.mvc._
import services.db.DBService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class ClientCategory(id: Option[Int], name: String) extends SimpleItem

class CategoriesController @Inject()(val db: DBService) extends Controller with Tables with SimpleItemsHelper[ClientCategory] {
  private implicit val table = categories

  implicit val categoryWrites: Writes[Category] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String]
    ) (unlift(Category.unapply))

  def allCategories: Action[AnyContent] = Action.async {
    allItems()
  }

  //noinspection MutatorLikeMethodIsParameterless
  def updateCategories: Action[JsValue] = Action.async(parse.json) { request =>
    updateItems(request)
  }

  protected override def update(id: Int, name: String): Future[Int] = updateName(id, name)

  protected override def create(name: String): Future[Int] = addCategory(name)

  protected override def deleteBesidesItems(ids: Seq[Int]): Future[Int] = deleteBesidesItems(ids)

  protected override def allItems(): Future[Result] = {
    sorted[Categories, Category].map(categories => {
      Ok(Json.toJson(categories))
    })
  }

  protected implicit val itemReads: Reads[ClientCategory] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String]
    ) (ClientCategory.apply _)
}
