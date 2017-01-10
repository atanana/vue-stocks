package controllers

import javax.inject.Inject

import models.db._
import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json.{Json, Writes, _}
import play.api.mvc._
import services.db.DBService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

//todo refactor with categories controller
class PacksController @Inject()(val db: DBService) extends Controller with Tables {
  private implicit val table = packs

  case class ClientPack(id: Option[Int], name: String)

  implicit val packWrites: Writes[Pack] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String]
    ) (unlift(Pack.unapply))

  implicit val clientCategoryReads: Reads[ClientPack] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String]
    ) (ClientPack.apply _)

  def allPacks: Action[AnyContent] = Action.async {
    allSorted
  }

  private def allSorted = {
    sorted[Packs, Pack].map(categories => {
      Ok(Json.toJson(categories))
    })
  }

  //noinspection MutatorLikeMethodIsParameterless
  def updatePacks: Action[JsValue] = Action.async(parse.json) { request =>
    Try(
      request.body.as[JsArray].value
        .map(_.as[ClientPack])
    ) match {
      case Success(newPacks) =>
        val newPacksIds = newPacks.map(_.id).filter(_.isDefined).map(_.get)
        Future.sequence(
          deleteBesides(newPacksIds) +: updateAndCreatePacks(newPacks)
        ).flatMap(_ => allSorted)
      case Failure(exception) =>
        Logger.error(s"Cannot parse request: ${request.body}", exception)
        Future(BadRequest)
    }
  }

  private def updateAndCreatePacks(newPacks: Seq[ClientPack]) = {
    newPacks.map(pack => pack.id
      .map(id => updateName(id, pack.name))
      .getOrElse(addPack(pack.name)))
  }
}
