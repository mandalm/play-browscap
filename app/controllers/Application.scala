package controllers

import play.api._
import play.api.mvc._

import models._

object Application extends Controller {

  val browsCap = BrowsCap()
  
  def index = Action {
    Ok(views.html.index(browsCap))
  }

  def browscap() = Action { request =>
    val ua = request.queryString.get("ua").flatMap(_.headOption)
    ua match {
      case None => BadRequest()
      case Some(x) => Redirect(routes.Application.browscapitem(browsCap.firstMatch(x).get.attr("InternalID").toInt))
    }
  }

  def browscapitem(id: Int) = Action {
    browsCap.getByInternalID(id) match {
      case None => NotFound
      case Some(x) => Ok(views.html.item(x, browsCap))
    }
  }

  def masterparent(name: String) = Action {
    browsCap.getMasterParentByName(java.net.URLDecoder.decode(name)) match {
      case None => NotFound
      case Some(x) => Ok(views.html.item(x, browsCap))
    }
  }
}
