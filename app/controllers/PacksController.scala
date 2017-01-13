package controllers

import javax.inject.Inject

import models.db._
import play.api.libs.functional.syntax._
import play.api.libs.json.{Json, Writes, _}
import play.api.mvc._
import services.db.DBService
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class ClientPack(id: Option[Int], name: String) extends SimpleItem

class PacksController @Inject()(val db: DBService) extends SimpleItemsHelper[ClientPack, Pack, Packs] {
  implicit val packWrites: Writes[Pack] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String]
    ) (unlift(Pack.unapply))

  protected override def create(name: String): Future[Int] = addPack(name)

  protected override def sortedItems(): Future[Result] = {
    sorted[Pack, Packs].map(categories => {
      Ok(Json.toJson(categories))
    })
  }

  protected implicit val itemReads: Reads[ClientPack] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String]
    ) (ClientPack.apply _)

  override protected implicit val table: TableQuery[Packs] = packs
}
