package controllers

import javax.inject.Inject

import models.db.{Product, Tables}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._
import services.db.DBService
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global

class ProductsController @Inject()(val db: DBService) extends Controller with Tables {
  private implicit val productsWrites: Writes[Product] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "productTypeId").write[Int] and
      (JsPath \ "categoryId").write[Int] and
      (JsPath \ "packId").write[Int]
    ) (unlift(Product.unapply))

  def allProducts: Action[AnyContent] = Action.async {
    db.runAsync(products.sortBy(_.id).result)
      .map(products => Ok(Json.toJson(products)))
  }
}