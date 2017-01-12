package controllers

import play.api.Logger
import play.api.libs.json.{JsArray, JsValue, Reads}
import play.api.mvc.Results.BadRequest
import play.api.mvc.{Request, Result}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

trait SimpleItem {
  val id: Option[Int]
  val name: String
}

trait SimpleItemsHelper[T <: SimpleItem] {

  protected def update(id: Int, name: String): Future[Int]

  protected def create(name: String): Future[Int]

  protected def deleteBesidesItems(ids: Seq[Int]): Future[Int]

  protected def allItems(): Future[Result]

  protected implicit val itemReads: Reads[T]

  protected def updateAndCreateItems(newItems: Seq[T]): Seq[Future[Int]] = {
    newItems.map(item => item.id
      .map(id => update(id, item.name))
      .getOrElse(create(item.name)))
  }

  protected def updateItems(request: Request[JsValue]): Future[Result] = {
    Try(
      request.body.as[JsArray].value
        .map(_.as[T])
    ) match {
      case Success(newPacks) =>
        val newItemsIds = newPacks.map(_.id).flatMap(_.toList)
        Future.sequence(
          deleteBesidesItems(newItemsIds) +: updateAndCreateItems(newPacks)
        ).flatMap(_ => allItems())
      case Failure(exception) =>
        Logger.error(s"Cannot parse request: ${request.body}", exception)
        Future(BadRequest)
    }
  }
}
