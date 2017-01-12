package controllers

import models.db.{Tables, WithIdColumn, WithNameColumn}
import play.api.Logger
import play.api.libs.json.{JsArray, JsValue, Reads}
import play.api.mvc._
import slick.driver.MySQLDriver.api.Table
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

trait SimpleItem {
  val id: Option[Int]
  val name: String
}

abstract class SimpleItemsHelper[C <: SimpleItem, T <: Table[_] with WithNameColumn with WithIdColumn] extends Controller with Tables {
  protected implicit val table: TableQuery[T]

  protected def create(name: String): Future[Int]

  protected def deleteBesidesItems(ids: Seq[Int]): Future[Int]

  protected def sortedItems(): Future[Result]

  protected implicit val itemReads: Reads[C]

  protected def updateAndCreateItems(newItems: Seq[C]): Seq[Future[Int]] = {
    newItems.map(item => item.id
      .map(id => updateName(id, item.name))
      .getOrElse(create(item.name)))
  }

  def updateItems(): Action[JsValue] = Action.async(parse.json) { request =>
    Try(
      request.body.as[JsArray].value
        .map(_.as[C])
    ) match {
      case Success(newPacks) =>
        val newItemsIds = newPacks.map(_.id).flatMap(_.toList)
        Future.sequence(
          deleteBesidesItems(newItemsIds) +: updateAndCreateItems(newPacks)
        ).flatMap(_ => sortedItems())
      case Failure(exception) =>
        Logger.error(s"Cannot parse request: ${request.body}", exception)
        Future(BadRequest)
    }
  }

  def allItems: Action[AnyContent] = Action.async {
    sortedItems()
  }
}
