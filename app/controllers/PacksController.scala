package controllers

import javax.inject.Inject

import models.db._
import play.api.libs.functional.syntax._
import play.api.libs.json.{Writes, _}
import services.db.DBService
import slick.lifted.TableQuery

import scala.concurrent.Future

case class ClientPack(id: Option[Int], name: String) extends SimpleItem

class PacksController @Inject()(val db: DBService) extends SimpleItemsHelper[ClientPack, Pack, Packs] {
  override protected implicit val table: TableQuery[Packs] = packs

  protected override def create(name: String): Future[Int] = addPack(name)

  protected implicit val itemReads: Reads[ClientPack] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String]
    ) (ClientPack.apply _)

  override protected implicit val itemWrites: Writes[Pack] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String]
    ) (unlift(Pack.unapply))
}
