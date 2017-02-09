package services.db

import javax.inject.{Inject, Singleton}

import models.db.{Pack, Packs}
import slick.driver.MySQLDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

case class ClientPack(id: Option[Int], name: String) extends SimpleItem

@Singleton
class PacksDao @Inject()(db: DBService) extends SimpleDao[ClientPack, Pack, Packs](db) {
  override protected val table: TableQuery[Packs] = TableQuery[Packs]

  override def createItem(pack: ClientPack): Future[Int] = {
    db.runAsync(table += Pack(0, pack.name))
  }
}
