package services.db

import models.db.{WithIdColumn, WithNameColumn}
import slick.driver.MySQLDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

trait SimpleItem {
  val id: Option[Int]
  val name: String
}

abstract class SimpleDao[ClientItemType <: SimpleItem, ItemType, TableType <: Table[ItemType] with WithNameColumn with WithIdColumn](db: DBService) {
  protected val table: TableQuery[TableType]

  def updateName(id: Int, name: String): Future[Int] = {
    db.runAsync(table.filter(_.id === id).map(_.name).update(name))
  }

  def deleteBesides(ids: Seq[Int]): Future[Int] = {
    db.runAsync(table.filterNot(_.id.inSet(ids)).delete)
  }

  def sorted(): Future[Seq[ItemType]] = {
    db.runAsync(table.sortBy(_.name.asc).result)
  }

  def createItem(item: ClientItemType): Future[Int]
}
