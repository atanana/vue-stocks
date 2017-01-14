package controllers

import javax.inject.Inject

import models.db._
import play.api.libs.functional.syntax._
import play.api.libs.json.{Writes, _}
import services.db.DBService
import slick.lifted.TableQuery

import scala.concurrent.Future

case class ClientProductType(id: Option[Int], name: String) extends SimpleItem

class ProductTypesController @Inject()(val db: DBService) extends SimpleItemsHelper[ClientProductType, ProductType, ProductTypes] {
  override protected implicit val table: TableQuery[ProductTypes] = productTypes

  protected override def create(name: String): Future[Int] = addProductType(name)

  protected implicit val itemReads: Reads[ClientProductType] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String]
    ) (ClientProductType.apply _)

  override protected implicit val itemWrites: Writes[ProductType] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String] and
      (JsPath \ "categoryId").writeNullable[Int]
    ) (unlift(ProductType.unapply))
}
