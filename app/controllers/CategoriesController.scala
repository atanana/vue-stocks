package controllers

import javax.inject.Inject

import models.db.{Category, Tables}
import play.api.libs.functional.syntax._
import play.api.libs.json.{Json, Writes, _}
import play.api.mvc._
import services.db.DBService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

class CategoriesController @Inject()(val db: DBService) extends Controller with Tables {
  implicit val categoryWrites: Writes[Category] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String]
    ) (unlift(Category.unapply))

  implicit val categoryReads: Reads[Category] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "name").read[String]
    ) (Category.apply _)

  def allCategories: Action[AnyContent] = Action.async {
    allSorted
  }

  private def allSorted = {
    sortedCategories.map(categories => {
      Ok(Json.toJson(categories))
    })
  }

  //noinspection MutatorLikeMethodIsParameterless
  def updateCategories: Action[JsValue] = Action.async(parse.json) { request =>
    Try(
      request.body.as[JsArray].value
        .map(_.as[Category])
    ) match {
      case Success(newCategories) =>
        Future.sequence(
          newCategories.map(category => updateCategoryName(category.id, category.name))
        ).flatMap(updated => allSorted)
      case Failure(exception) =>
        Future(BadRequest)
    }
  }
}
