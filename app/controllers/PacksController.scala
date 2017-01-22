package controllers

import javax.inject.Inject

import models.db._
import play.api.libs.functional.syntax._
import play.api.libs.json.{Writes, _}
import services.db.DBService
import slick.driver.MySQLDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

case class ClientPack(id: Option[Int], name: String) extends SimpleItem

class PacksController @Inject()(val db: DBService, val authorizedAction: AuthorizedAction) extends SimpleItemsHelper[ClientPack, Pack, Packs] {
  override protected implicit val table: TableQuery[Packs] = packs

  protected override def createItem(item: ClientPack): Future[Int] = db.runAsync(packs += Pack(0, item.name))

  protected implicit val itemReads: Reads[ClientPack] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String]
    ) (ClientPack.apply _)

  override protected implicit val itemWrites: Writes[Pack] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String]
    ) (unlift(Pack.unapply))
}
