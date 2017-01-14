package controllers

import javax.inject.Inject

import models.db._
import play.api.libs.functional.syntax._
import play.api.libs.json.{Writes, _}
import services.db.DBService
import slick.driver.MySQLDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

case class ClientProductType(id: Option[Int], name: String, categoryId: Option[Int]) extends SimpleItem

class ProductTypesController @Inject()(val db: DBService) extends SimpleItemsHelper[ClientProductType, ProductType, ProductTypes] {
  override protected implicit val table: TableQuery[ProductTypes] = productTypes

  protected override def createItem(item: ClientProductType): Future[Int] =
    db.runAsync(productTypes += ProductType(0, item.name, item.categoryId))

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

  override protected def updateItem(item: ClientProductType, id: Int): Future[Int] = {
    val query = productTypes
      .filter(_.id === id)
      .map(productType => (productType.name, productType.categoryId))
      .update((item.name, item.categoryId))
    db.runAsync(query)
  }
}
