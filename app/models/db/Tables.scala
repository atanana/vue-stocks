package models.db

import services.db.DBService
import slick.driver.MySQLDriver.api._
import slick.lifted.{ProvenShape, TableQuery}

import scala.concurrent.Future

case class Category(id: Int, name: String)

class Categories(tag: Tag) extends Table[Category](tag, "categories") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("name")

  def * : ProvenShape[Category] = (id, name) <> (Category.tupled, Category.unapply)
}

trait Tables {
  val db: DBService

  val categories: TableQuery[Categories] = TableQuery[Categories]

  def sortedCategories: Future[Seq[Category]] = {
    db.runAsync(categories.sortBy(_.name.asc).result)
  }

  def updateCategoryName(id: Int, name: String): Future[Int] = {
    db.runAsync(categories.filter(_.id === id).map(_.name).update(name))
  }
}