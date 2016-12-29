package controllers

import javax.inject.Inject

import models.db.{Category, Tables}
import play.api.libs.json.{Json, Writes}
import play.api.mvc._
import services.db.DBService

import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.concurrent.ExecutionContext.Implicits.global

class CategoriesController @Inject()(val db: DBService) extends Controller with Tables {
  implicit val categoryWrites: Writes[Category] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String]
    ) (unlift(Category.unapply))

  def allCategories: Action[AnyContent] = Action.async {
    sortedCategories.map(categories => {
      Ok(
        Json.arr(categories)
      )
    })
  }
}
