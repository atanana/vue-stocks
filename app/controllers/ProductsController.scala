package controllers

import play.api.libs.json.Json
import play.api.mvc._

class ProductsController extends Controller {
  def allProducts = Action {
    Ok(Json.arr(
      Json.obj("name" -> "test 1"),
      Json.obj("name" -> "test 2"),
      Json.obj("name" -> "test 3")
    ))
  }
}
