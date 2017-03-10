package services.db

import javax.inject.{Inject, Singleton}

import models.db.{CustomColumns, MenuItem, MenuItems}
import org.joda.time.LocalDate
import slick.driver.MySQLDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

case class ClientMenuItem(id: Option[Int], name: String, date: LocalDate) extends SimpleItem

@Singleton
class MenuItemsDao @Inject()(db: DBService) extends SimpleDao[ClientMenuItem, MenuItem, MenuItems](db) {
  private implicit val localDateColumn = CustomColumns.localDateColumn

  override protected val table: TableQuery[MenuItems] = TableQuery[MenuItems]

  override def createItem(menuItem: ClientMenuItem): Future[Int] = {
    db.runAsync(table += MenuItem(0, menuItem.name, menuItem.date))
  }

  def updateMenuItem(item: ClientMenuItem, id: Int): Future[Int] = {
    val query = table
      .filter(_.id === id)
      .map(menuItem => (menuItem.name, menuItem.date))
      .update((item.name, item.date))
    db.runAsync(query)
  }
}
