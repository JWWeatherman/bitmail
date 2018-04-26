package actors.messages

import play.api.libs.json._

case class SendRequest(recipientEmail: String,
                       senderEmail: String,
                       senderMessage: String,
                       remainAnonymous: Boolean,
                       kind: String = SendRequest.kind.name)
    extends WebSocketMessage

object SendRequest {
  final val kind = 'SendRequest

  implicit val sendRequestFormat = Json.format[SendRequest]
}
