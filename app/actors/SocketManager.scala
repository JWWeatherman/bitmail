package actors

import actors.messages.{ RequestSessionInfo, RequestSessionInfoWithActor, SendRequest }
import akka.actor.{ Actor, ActorRef, Props }
import com.google.inject.Inject
import com.google.inject.name.Named
import play.api.libs.json.{ JsObject, JsValue }

@Named(ActorNames.SocketManager)
class SocketManager @Inject()(val out : ActorRef, val sessionController: ActorRef) extends Actor {
  override def receive : Receive = {
    case msg : JsValue =>
      msg.as[JsObject].value("kind").as[String] match {
        case SendRequest.kind.name =>
          import SendRequest._
          val sr = msg.as[SendRequest]
        case RequestSessionInfo.kind.name =>
          sessionController ! RequestSessionInfoWithActor(out)
        case s =>
          printf(s)
      }
  }
}

object SocketManager {
  def props(out : ActorRef, sessionController: ActorRef) = Props( new SocketManager(out, sessionController))
}
