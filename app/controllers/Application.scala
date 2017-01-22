package controllers

import javax.inject.Inject

import play.api.mvc._

class Application @Inject()(val authorizedAction: AuthorizedAction) extends Controller {

  def index(path: String): Action[AnyContent] = authorizedAction {
    Ok(views.html.index())
  }
}