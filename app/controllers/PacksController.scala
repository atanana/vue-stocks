package controllers

import javax.inject.Inject

import models.db._
import play.api.libs.functional.syntax._
import play.api.libs.json.{Writes, _}
import services.db.{ClientPack, DBService, PacksDao}

import scala.concurrent.Future

class PacksController @Inject()(val db: DBService, val authorizedAction: AuthorizedAction, packsDao: PacksDao) extends SimpleItemsHelper[ClientPack, Pack, Packs](packsDao) {
  protected override def createItem(pack: ClientPack): Future[Int] = packsDao.addPack(pack)

  protected implicit val itemReads: Reads[ClientPack] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String]
    ) (ClientPack.apply _)

  override protected implicit val itemWrites: Writes[Pack] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String]
    ) (unlift(Pack.unapply))
}
