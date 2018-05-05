package actors.messages.ws

import play.api.libs.json._

case class SendRequest(recipientEmail: String,
                       senderEmail: String,
                       senderMessage: String,
                       remainAnonymous: Boolean)
    extends WebSocketMessage {
  override def kind : Symbol = SendRequest.kind
}

object SendRequest {
  final val kind = 'SendRequest

  implicit val sendRequestFormat = Json.format[SendRequest]
}
