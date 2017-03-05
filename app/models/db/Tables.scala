package models.db

import java.sql.Timestamp

import models.db.ProductLogAction.ProductLogAction
import org.joda.time.{DateTime, DateTimeZone}
import slick.ast.BaseTypedType
import slick.driver.MySQLDriver.api._
import slick.jdbc.JdbcType
import slick.lifted.ProvenShape

object CustomColumns {
  val dateTimeColumn: JdbcType[DateTime] with BaseTypedType[DateTime] = MappedColumnType.base[DateTime, Timestamp](
    dateTime => new Timestamp(dateTime.withZone(DateTimeZone.UTC).getMillis),
    timestamp => new DateTime(timestamp.getTime, DateTimeZone.UTC)
  )
}

case class Category(id: Int, name: String)

case class Pack(id: Int, name: String)

case class ProductType(id: Int, name: String, categoryId: Option[Int])

case class Product(id: Int, productTypeId: Int, categoryId: Int, packId: Int)

object ProductLogAction extends Enumeration {
  type ProductLogAction = Value
  val add = Value("add")
  val remove = Value("remove")
}

case class ProductLogEntry(id: Int, productTypeId: Int, categoryId: Int, packId: Int, action: ProductLogAction, time: DateTime)

trait WithNameColumn {
  def name: Rep[String]
}

trait WithIdColumn {
  def id: Rep[Int]
}

class Categories(tag: Tag) extends Table[Category](tag, "categories") with WithNameColumn with WithIdColumn {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("name")

  def * : ProvenShape[Category] = (id, name) <> (Category.tupled, Category.unapply)
}

class Packs(tag: Tag) extends Table[Pack](tag, "packs") with WithNameColumn with WithIdColumn {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("name")

  def * : ProvenShape[Pack] = (id, name) <> (Pack.tupled, Pack.unapply)
}

class ProductTypes(tag: Tag) extends Table[ProductType](tag, "product_types") with WithNameColumn with WithIdColumn {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("name")

  def categoryId: Rep[Option[Int]] = column[Option[Int]]("category_id")

  def * : ProvenShape[ProductType] = (id, name, categoryId) <> (ProductType.tupled, ProductType.unapply)
}

class Products(tag: Tag) extends Table[Product](tag, "products") with WithIdColumn {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def productTypeId: Rep[Int] = column[Int]("product_type_id")

  def categoryId: Rep[Int] = column[Int]("category_id")

  def packId: Rep[Int] = column[Int]("pack_id")

  def * : ProvenShape[Product] = (id, productTypeId, categoryId, packId) <> (Product.tupled, Product.unapply)
}

class ProductLogs(tag: Tag) extends Table[ProductLogEntry](tag, "logs") with WithIdColumn {
  private implicit val dateTimeColumn = CustomColumns.dateTimeColumn

  private implicit val logActionMapper: JdbcType[ProductLogAction] with BaseTypedType[ProductLogAction] = MappedColumnType.base[ProductLogAction, String](
    value => value.toString,
    string => ProductLogAction.withName(string)
  )

  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def productTypeId: Rep[Int] = column[Int]("product_type_id")

  def categoryId: Rep[Int] = column[Int]("category_id")

  def packId: Rep[Int] = column[Int]("pack_id")

  def action: Rep[ProductLogAction] = column[ProductLogAction]("action")

  def time: Rep[DateTime] = column[DateTime]("time")

  def * : ProvenShape[ProductLogEntry] = (id, productTypeId, categoryId, packId, action, time) <> (ProductLogEntry.tupled, ProductLogEntry.unapply)
}
