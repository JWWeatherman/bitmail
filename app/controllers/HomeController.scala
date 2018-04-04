package controllers

import javax.inject._

import play.api.libs.json._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

class HomeController @Inject()() extends Controller {

  def triggerCompile = Action {
    Ok(Json.obj("compile_message" -> "RELOAD COMPILE"))
  }
}