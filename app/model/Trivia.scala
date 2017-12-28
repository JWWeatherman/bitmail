package model

import java.util.Locale.Category
import javax.inject.Inject

import akka.actor.FSM.Failure

import scala.concurrent.{ExecutionContext, Future}
import play.api.Logger
import play.api.mvc.{Action, AnyContent, Controller, Request}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.mvc.BodyParser.AnyContent
import reactivemongo.core.errors.ReactiveMongoException
// Reactive Mongo imports
import reactivemongo.api.Cursor
import play.modules.reactivemongo.{ // ReactiveMongo Play2 plugin
MongoController,
ReactiveMongoApi,
ReactiveMongoComponents
}
// BSON-JSON conversions/collection
import reactivemongo.play.json._, collection._
import scala.io.Source._
import scala.io._
import play.api.libs.json.{Json, Format, JsObject}

import scala.util._

trait Trivia extends MongoController with ReactiveMongoComponents with Controller {

}
