package actors.messages

import play.api.libs.json._

case class SendRequest(kind: String,
                       recipientEmail: String,
                       senderEmail: String,
                       senderMessage: String,
                       remainAnonymous: Boolean)
    extends WebSocketMessage

object SendRequest {
  final val kind = 'SendRequest

  implicit val sendRequestForm = Json.format[SendRequest]
}