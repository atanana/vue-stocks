package services.db

import javax.inject.{Inject, Singleton}

import models.db.{ProductType, ProductTypes}
import slick.driver.MySQLDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

case class ClientProductType(id: Option[Int], name: String, categoryId: Option[Int]) extends SimpleItem

@Singleton
class ProductTypesDao @Inject()(db: DBService) extends SimpleDao[ClientProductType, ProductType, ProductTypes](db) {
  override protected val table: TableQuery[ProductTypes] = TableQuery[ProductTypes]

  def addProductType(productType: ClientProductType): Future[Int] = {
    db.runAsync(table += ProductType(0, productType.name, productType.categoryId))
  }

  def updateProductType(item: ClientProductType, id: Int): Future[Int] = {
    val query = table
      .filter(_.id === id)
      .map(productType => (productType.name, productType.categoryId))
      .update((item.name, item.categoryId))
    db.runAsync(query)
  }
}
