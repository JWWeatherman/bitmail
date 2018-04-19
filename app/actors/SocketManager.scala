package actors

import actors.messages.SendRequest
import akka.actor.{ Actor, ActorRef, Props }
import play.api.libs.json.{ JsObject, JsValue }

class SocketManager(val out : ActorRef) extends Actor {
  override def receive : Receive = {
    case msg : JsValue =>
      msg.as[JsObject].value("kind").as[String] match {
        case SendRequest.kind.name =>
          import SendRequest._
          val sr = msg.as[SendRequest]
      }
  }
}

object SocketManager {
  def props(out : ActorRef) = Props( new SocketManager(out))
}
