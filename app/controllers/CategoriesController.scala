package controllers

import javax.inject.Inject

import models.db.{Categories, Category, Tables}
import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json.{Json, Writes, _}
import play.api.mvc._
import services.db.DBService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class CategoriesController @Inject()(val db: DBService) extends Controller with Tables {
  private implicit val table = categories

  case class ClientCategory(id: Option[Int], name: String)

  implicit val categoryWrites: Writes[Category] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String]
    ) (unlift(Category.unapply))

  implicit val clientCategoryReads: Reads[ClientCategory] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String]
    ) (ClientCategory.apply _)

  def allCategories: Action[AnyContent] = Action.async {
    allSorted
  }

  private def allSorted = {
    sorted[Categories, Category].map(categories => {
      Ok(Json.toJson(categories))
    })
  }

  //noinspection MutatorLikeMethodIsParameterless
  def updateCategories: Action[JsValue] = Action.async(parse.json) { request =>
    Try(
      request.body.as[JsArray].value
        .map(_.as[ClientCategory])
    ) match {
      case Success(newCategories) =>
        val newCategoriesIds = newCategories.map(_.id).filter(_.isDefined).map(_.get)
        Future.sequence(
          deleteBesides(newCategoriesIds) +: updateAndCreateCategories(newCategories)
        ).flatMap(_ => allSorted)
      case Failure(exception) =>
        Logger.error(s"Cannot parse request: ${request.body}", exception)
        Future(BadRequest)
    }
  }

  private def updateAndCreateCategories(newCategories: Seq[ClientCategory]) = {
    newCategories.map(category => category.id
      .map(id => updateName(id, category.name))
      .getOrElse(addCategory(category.name)))
  }
}
