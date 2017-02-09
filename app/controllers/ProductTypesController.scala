package controllers

import javax.inject.Inject

import models.db._
import play.api.libs.functional.syntax._
import play.api.libs.json.{Writes, _}
import services.db.{ClientProductType, ProductTypesDao}

import scala.concurrent.Future

class ProductTypesController @Inject()(val authorizedAction: AuthorizedAction, productTypesDao: ProductTypesDao)
  extends SimpleItemsHelper[ClientProductType, ProductType, ProductTypes](productTypesDao) {

  protected implicit val itemReads: Reads[ClientProductType] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "categoryId").readNullable[Int]
    ) (ClientProductType.apply _)

  override protected implicit val itemWrites: Writes[ProductType] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String] and
      (JsPath \ "categoryId").writeNullable[Int]
    ) (unlift(ProductType.unapply))

  override protected def updateItem(item: ClientProductType, id: Int): Future[Int] = productTypesDao.updateProductType(item, id)
}
