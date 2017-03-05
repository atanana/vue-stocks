package services.db

import javax.inject.{Inject, Singleton}

import models.db.ProductLogAction.ProductLogAction
import models.db.{CustomColumns, ProductLogAction, ProductLogEntry, ProductLogs}
import org.joda.time.DateTime
import slick.driver.MySQLDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

@Singleton
class ProductsLogsDao @Inject()(db: DBService) {
  private implicit val dateTimeColumn = CustomColumns.dateTimeColumn

  private val productLogs: TableQuery[ProductLogs] = TableQuery[ProductLogs]

  def addProduct(product: ClientProduct): Future[Int] = {
    logActionOnProduct(product, ProductLogAction.add)
  }

  def deleteProduct(product: ClientProduct): Future[Int] = {
    logActionOnProduct(product, ProductLogAction.remove)
  }

  private def logActionOnProduct(product: ClientProduct, action: ProductLogAction) = {
    db.runAsync(productLogs += ProductLogEntry(0, product.productTypeId, product.categoryId, product.packId, action, DateTime.now()))
  }

  def allLogs: Future[Seq[ProductLogEntry]] = {
    db.runAsync(productLogs.sortBy(_.time.desc).result)
  }
}
