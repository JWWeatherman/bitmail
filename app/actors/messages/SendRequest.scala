package actors.messages

import play.api.libs.json._

case class SendRequest(kind: String = SendRequest.kind.name,
                       recipientEmail: String,
                       senderEmail: String,
                       senderMessage: String,
                       remainAnonymous: Boolean)
    extends WebSocketMessage

object SendRequest {
  final val kind = 'SendRequest

  implicit val sendRequestFormat = Json.format[SendRequest]
}