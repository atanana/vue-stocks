package controllers

import javax.inject.Inject

import models.db.{Categories, Category}
import play.api.libs.functional.syntax._
import play.api.libs.json.{Writes, _}
import services.db.{CategoriesDao, ClientCategory, DBService}

import scala.concurrent.Future

class CategoriesController @Inject()(val db: DBService, val authorizedAction: AuthorizedAction, categoriesDao: CategoriesDao)
  extends SimpleItemsHelper[ClientCategory, Category, Categories](categoriesDao) {
  protected override def createItem(item: ClientCategory): Future[Int] = categoriesDao.addCategory(item)

  protected implicit val itemReads: Reads[ClientCategory] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String]
    ) (ClientCategory.apply _)

  override protected implicit val itemWrites: Writes[Category] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String]
    ) (unlift(Category.unapply))
}
