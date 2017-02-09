package controllers

import javax.inject.Inject

import models.db._
import play.api.libs.functional.syntax._
import play.api.libs.json.{Writes, _}
import services.db.{ClientPack, PacksDao}

class PacksController @Inject()(val authorizedAction: AuthorizedAction, packsDao: PacksDao)
  extends SimpleItemsHelper[ClientPack, Pack, Packs](packsDao) {

  protected implicit val itemReads: Reads[ClientPack] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String]
    ) (ClientPack.apply _)

  override protected implicit val itemWrites: Writes[Pack] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String]
    ) (unlift(Pack.unapply))
}
