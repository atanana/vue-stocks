package controllers

import play.api.mvc._

class Application extends Controller {

  def index = AuthorizedAction {
    Ok(views.html.index())
  }
}