package services.db

import javax.inject.{Inject, Singleton}

import models.db.{Product, Products}
import slick.driver.MySQLDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

case class ClientProduct(categoryId: Int, productTypeId: Int, packId: Int)

@Singleton
class ProductsDao @Inject()(db: DBService) {
  private val products: TableQuery[Products] = TableQuery[Products]

  def addProduct(product: ClientProduct): Future[Int] = {
    db.runAsync(products += Product(0, product.productTypeId, product.categoryId, product.packId))
  }

  def allProducts: Future[Seq[Product]] = {
    db.runAsync(products.sortBy(_.id).result)
  }
}
