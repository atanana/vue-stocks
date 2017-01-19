package controllers

import javax.inject.Inject

import models.db.{Product, Tables}
import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._
import services.db.DBService
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class ProductsController @Inject()(val db: DBService) extends Controller with Tables {
  private implicit val productsWrites: Writes[Product] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "productTypeId").write[Int] and
      (JsPath \ "categoryId").write[Int] and
      (JsPath \ "packId").write[Int]
    ) (unlift(Product.unapply))

  def allProducts: Action[AnyContent] = Action.async {
    getAllProducts
  }

  private def getAllProducts = {
    db.runAsync(products.sortBy(_.id).result)
      .map(products => {
        val data = groupProducts(products)
          .map((toJson _).tupled)
        Ok(Json.toJson(data))
      })
  }

  private def groupProducts(products: Seq[Product]) = {
    products
      .groupBy(product => (product.productTypeId, product.categoryId))
      .map({ case ((productType, category), productsGroup) =>
        groupByPacks(productType, category, productsGroup)
      })
  }

  private def groupByPacks(productType: Int, category: Int, productsGroup: Seq[Product]) = {
    val packs = productsGroup.groupBy(_.packId)
      .map({ case (packId, productsPackGroup) =>
        (packId, productsPackGroup.size)
      })
    (productType, category, packs)
  }

  private def toJson(productTypeId: Int, categoryId: Int, packs: Map[Int, Int]) = {
    Json.obj(
      "productTypeId" -> productTypeId,
      "categoryId" -> categoryId,
      "packs" -> packs.map({ case (pack, quantity) =>
        Json.obj(
          "packId" -> pack,
          "quantity" -> quantity
        )
      })
    )
  }

  case class NewProduct(categoryId: Int, productTypeId: Int, packId: Int)

  implicit val newProductReads: Reads[NewProduct] = (
    (JsPath \ "categoryId").read[Int] and
      (JsPath \ "productTypeId").read[Int] and
      (JsPath \ "packId").read[Int]
    ) (NewProduct.apply _)

  def addProduct(): Action[JsValue] = Action.async(parse.json) { request =>
    Try(request.body.as[NewProduct]) match {
      case Success(newProduct) => insertProduct(newProduct).flatMap(_ => getAllProducts)
      case Failure(exception) =>
        Logger.error(s"Cannot parse request: ${request.body}", exception)
        Future(BadRequest)
    }
  }

  private def insertProduct(product: NewProduct) = {
    db.runAsync(products += Product(0, product.productTypeId, product.categoryId, product.packId))
  }
}