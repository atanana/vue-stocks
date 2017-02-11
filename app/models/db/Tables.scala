package models.db

import slick.driver.MySQLDriver.api._
import slick.lifted.ProvenShape

case class Category(id: Int, name: String)

case class Pack(id: Int, name: String)

case class ProductType(id: Int, name: String, categoryId: Option[Int])

case class Product(id: Int, productTypeId: Int, categoryId: Int, packId: Int)

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
