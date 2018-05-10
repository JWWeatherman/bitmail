package actors

import actors.messages.ws.SendRequest
import akka.actor.Actor

class RequestGenerator extends Actor {
  override def receive : Receive = {
    case SendRequest(sessionId, recipientEmail, senderEmail, senderMessage, remainAnonymous) =>
val i = 1
  }
}
