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

  case class ClientProduct(categoryId: Int, productTypeId: Int, packId: Int)

  implicit val clientProductReads: Reads[ClientProduct] = (
    (JsPath \ "categoryId").read[Int] and
      (JsPath \ "productTypeId").read[Int] and
      (JsPath \ "packId").read[Int]
    ) (ClientProduct.apply _)

  def addProduct(): Action[JsValue] = actionOnProduct(insertProduct)

  def deleteProduct(): Action[JsValue] = actionOnProduct(deleteProduct)

  private def insertProduct(product: ClientProduct) = {
    db.runAsync(products += Product(0, product.productTypeId, product.categoryId, product.packId))
  }

  private def deleteProduct(product: ClientProduct) = {
    db.runAsync(
      products.filter(dbProduct => dbProduct.productTypeId === product.productTypeId
        && dbProduct.categoryId === product.categoryId
        && dbProduct.packId === product.packId)
        .take(1)
        .delete
    )
  }

  private def actionOnProduct(action: (ClientProduct) => Future[Int]): Action[JsValue] = Action.async(parse.json) { request =>
    Try(request.body.as[ClientProduct]) match {
      case Success(product) => action(product).flatMap(_ => getAllProducts)
      case Failure(exception) =>
        Logger.error(s"Cannot parse request: ${request.body}", exception)
        Future(BadRequest)
    }
  }
}