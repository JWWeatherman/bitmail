package actors.messages.ws

import dataentry.utility.SecureIdentifier
import play.api.libs.json._

case class SendRequest(sessionId : SecureIdentifier,
                       recipientEmail: String,
                       senderEmail: String,
                       senderMessage: String,
                       remainAnonymous: Boolean)
    extends WebSocketMessage {
  override def kind : Symbol = SendRequest.kind
}

object SendRequest {
  final val kind = 'sendRequest

  implicit val sendRequestFormat = Json.format[SendRequest]
}
