package actors

import actors.messages._
import actors.messages.ws._
import akka.actor.{ Actor, ActorRef, Props }
import com.google.inject.Inject
import com.google.inject.name.Named
import dataentry.utility.SecureIdentifier

// Never injected, created instead by the play SocketContoller using the props method below
class SocketManager(val out: ActorRef,
                              val sessionController: ActorRef,
                              val requestGenerator: ActorRef) extends Actor {

  var sessionId = Option.empty[SecureIdentifier]

  override def receive: Receive = {
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
    case msg: SendRequest =>
      requestGenerator ! msg
    case msg: UnknownMessage =>
      println(s"unknown message: ${msg.json.toString()}")
  }
}

object SocketManager {
  def props(out: ActorRef, sessionController: ActorRef, requestGenerator: ActorRef) = Props(new SocketManager(out, sessionController, requestGenerator))
}
