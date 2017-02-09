package controllers

import models.db._
import play.api.Logger
import play.api.libs.json._
import play.api.mvc._
import services.db.{SimpleDao, SimpleItem}
import slick.driver.MySQLDriver.api.Table

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

abstract class SimpleItemsHelper[ClientItemType <: SimpleItem, ItemType, TableType <: Table[ItemType]
  with WithNameColumn with WithIdColumn](dao: SimpleDao[ClientItemType, ItemType, TableType]) extends Controller {
  protected val authorizedAction: AuthorizedAction

  protected def createItem(item: ClientItemType): Future[Int]

  protected implicit val itemReads: Reads[ClientItemType]

  protected implicit val itemWrites: Writes[ItemType]

  private def updateAndCreateItems(newItems: Seq[ClientItemType]): Seq[Future[Int]] = {
    newItems.map(item => item.id
      .map(id => updateItem(item, id))
      .getOrElse(createItem(item)))
  }

  protected def updateItem(item: ClientItemType, id: Int): Future[Int] = {
    dao.updateName(id, item.name)
  }

  private def sortedItems(): Future[Result] = {
    dao.sorted().map(items => {
      Ok(Json.toJson(items))
    })
  }

  def updateItems(): Action[JsValue] = authorizedAction.async(parse.json) { request =>
    Try(
      request.body.as[JsArray].value
        .map(_.as[ClientItemType])
    ) match {
      case Success(newPacks) =>
        val newItemsIds = newPacks.map(_.id).flatMap(_.toList)
        Future.sequence(
          dao.deleteBesides(newItemsIds) +: updateAndCreateItems(newPacks)
        ).flatMap(_ => sortedItems())
      case Failure(exception) =>
        Logger.error(s"Cannot parse request: ${request.body}", exception)
        Future(BadRequest)
    }
  }

  def allItems: Action[AnyContent] = authorizedAction.async {
    sortedItems()
  }
}
