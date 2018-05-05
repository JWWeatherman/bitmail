package actors

import actors.messages._
import actors.messages.ws.{ ProvideSessionInfo, RequestSessionInfo, ResumeSession, SendRequest }
import akka.actor.{ Actor, ActorRef, Props }
import com.google.inject.Inject
import com.google.inject.name.Named
import dataentry.utility.SecureIdentifier
import play.api.libs.json.{ JsObject, JsValue, Json }

@Named(ActorNames.SocketManager)
class SocketManager @Inject()(val out: ActorRef, val sessionController: ActorRef) extends Actor {

  var sessionId = Option.empty[SecureIdentifier]

  override def receive: Receive = {
    case msg: SendRequest =>
      val sr = msg
    case msg: ProvideSessionInfo =>
      sessionId = Some(msg.sessionId)
      out ! msg
    case msg : RequestSessionInfo =>
      sessionId = Option.empty[SecureIdentifier]
      sessionController ! msg
    case msg : ResumeSession =>
      sessionController ! msg
    case msg: InvalidSession =>
      sessionController ! RequestSessionInfo
  }
}

object SocketManager {
  def props(out: ActorRef, sessionController: ActorRef) = Props(new SocketManager(out, sessionController))
}
