package controllers

import javax.inject.Inject

import models.db._
import play.api.libs.functional.syntax._
import play.api.libs.json.{Json, Writes, _}
import play.api.mvc._
import services.db.DBService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class ClientPack(id: Option[Int], name: String) extends SimpleItem

class PacksController @Inject()(val db: DBService) extends Controller with Tables with SimpleItemsHelper[ClientPack] {
  private implicit val table = packs

  implicit val packWrites: Writes[Pack] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String]
    ) (unlift(Pack.unapply))

  def allPacks: Action[AnyContent] = Action.async {
    allItems()
  }

  //noinspection MutatorLikeMethodIsParameterless
  def updatePacks: Action[JsValue] = Action.async(parse.json) { request =>
    updateItems(request)
  }

  protected override def update(id: Int, name: String): Future[Int] = updateName(id, name)

  protected override def create(name: String): Future[Int] = addPack(name)

  protected override def deleteBesidesItems(ids: Seq[Int]): Future[Int] = deleteBesidesItems(ids)

  protected override def allItems(): Future[Result] = {
    sorted[Packs, Pack].map(categories => {
      Ok(Json.toJson(categories))
    })
  }

  protected implicit val itemReads: Reads[ClientPack] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String]
    ) (ClientPack.apply _)
}
