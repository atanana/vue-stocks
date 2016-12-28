package controllers

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.mvc._

class ProductsController extends Controller {
  implicit val productQuantityWrites: Writes[ProductQuantity] = (
    (JsPath \ "pack").write[String] and
      (JsPath \ "quantity").write[Int]
    ) (unlift(ProductQuantity.unapply))
  implicit val productWrites: Writes[Product] = (
    (JsPath \ "name").write[String] and
      (JsPath \ "category").write[String] and
      (JsPath \ "items").write[List[ProductQuantity]]
    ) (unlift(Product.unapply))

  def allProducts = Action {
    Ok(Json.arr(
      Product("product 1", "category 1", List(
        ProductQuantity("pack 1", 2),
        ProductQuantity("pack 2", 3)
      )),
      Product("product 2", "category 2", List(
        ProductQuantity("pack 2", 1)
      ))
    ))
  }
}

case class Product(name: String, category: String, items: List[ProductQuantity])

case class ProductQuantity(pack: String, quantity: Int)