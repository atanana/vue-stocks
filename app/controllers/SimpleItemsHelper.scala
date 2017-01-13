package controllers

import models.db._
import play.api.Logger
import play.api.libs.json._
import play.api.mvc._
import slick.driver.MySQLDriver.api.Table
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

//todo use always simple item instead of custom client items?
trait SimpleItem {
  val id: Option[Int]
  val name: String
}

abstract class SimpleItemsHelper[ClientItemType <: SimpleItem, ItemType, TableType <: Table[ItemType] with WithNameColumn with WithIdColumn] extends Controller with Tables {
  protected implicit val table: TableQuery[TableType]

  protected def create(name: String): Future[Int]

  protected implicit val itemReads: Reads[ClientItemType]

  protected implicit val itemWrites: Writes[ItemType]

  private def updateAndCreateItems(newItems: Seq[ClientItemType]): Seq[Future[Int]] = {
    newItems.map(item => item.id
      .map(id => updateName(id, item.name))
      .getOrElse(create(item.name)))
  }

  private def sortedItems(): Future[Result] = {
    sorted[ItemType, TableType].map(items => {
      Ok(Json.toJson(items))
    })
  }

  def updateItems(): Action[JsValue] = Action.async(parse.json) { request =>
    Try(
      request.body.as[JsArray].value
        .map(_.as[ClientItemType])
    ) match {
      case Success(newPacks) =>
        val newItemsIds = newPacks.map(_.id).flatMap(_.toList)
        Future.sequence(
          deleteBesides(newItemsIds) +: updateAndCreateItems(newPacks)
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
