package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def browscap() = Action { request =>
    val ua = request.queryString.get("ua").flatMap(_.headOption)
    ua match {
      case None => BadRequest()
      case Some(x) => Ok("UA is: " + x)
    }
  }
}
