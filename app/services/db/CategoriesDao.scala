package services.db

import javax.inject.{Inject, Singleton}

import models.db.{Categories, Category}
import slick.driver.MySQLDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

case class ClientCategory(id: Option[Int], name: String) extends SimpleItem

@Singleton
class CategoriesDao @Inject()(db: DBService) extends SimpleDao[ClientCategory, Category, Categories](db) {
  override protected val table: TableQuery[Categories] = TableQuery[Categories]

  def addCategory(category: ClientCategory): Future[Int] = {
    db.runAsync(table += Category(0, category.name))
  }
}
