package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.routing._

@Singleton
class JsController @Inject() extends Controller {

  def javascriptRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("jsRoutes")(
        routes.javascript.HomeController.main,
        routes.javascript.HomeController.sender,
        routes.javascript.HomeController.recipient
      )
    ).as("text/javascript")
  }
}
