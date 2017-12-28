package controllers

import javax.inject._

import play.api._
import play.api.mvc._
import play.api.libs.json._
import model._
import play.api.libs.json
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

class HomeController @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller with Trivia {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def sender() = Action { implicit  request: Request[AnyContent] =>
    Redirect("/#/sender")
  }

  def recipient() = Action { implicit  request: Request[AnyContent] =>
    Redirect("/#/recipient")
  }

  def main() = Action { implicit  request: Request[AnyContent] =>
    Redirect("/#/main")
  }
}