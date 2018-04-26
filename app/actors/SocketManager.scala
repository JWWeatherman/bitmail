package actors

import actors.messages._
import akka.actor.{Actor, ActorRef, Props}
import com.google.inject.Inject
import com.google.inject.name.Named
import play.api.libs.json.{JsObject, JsValue, Json}

@Named(ActorNames.SocketManager)
class SocketManager @Inject()(val out: ActorRef, val sessionController: ActorRef) extends Actor {

  var sessionId = Option.empty[String]

  override def receive: Receive = {
    case msg: JsValue =>
      msg.as[JsObject].value("kind").as[String] match {
        case SendRequest.kind.name =>
          import SendRequest._
          val sr = msg.as[SendRequest]
        case RequestSessionInfo.kind.name =>
          sessionId = Option.empty[String]
          sessionController ! RequestSessionInfo()
        case ResumeSession.kind.name =>
          import ResumeSession._
          sessionController ! msg.as[ResumeSession]
        case s =>
          printf(s)
      }
    case msg: ProvideSessionInfo => {
      sessionId = Some(msg.sessionId)
      import ProvideSessionInfo._
      out ! Json.toJson(msg)
    }
    case msg: InvalidSession =>
      sessionController ! RequestSessionInfo
  }
}

object SocketManager {
  def props(out: ActorRef, sessionController: ActorRef) = Props(new SocketManager(out, sessionController))
}
