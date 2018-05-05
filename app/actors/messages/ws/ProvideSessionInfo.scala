package actors.messages.ws

import dataentry.utility.SecureIdentifier
import play.api.libs.json.Json

// Sent to the client to provide new or existing session information
case class ProvideSessionInfo(sessionId: SecureIdentifier) extends WebSocketMessage {
  override def kind : Symbol = ProvideSessionInfo.kind
}

object ProvideSessionInfo {
  final val kind = 'provideSessionInfo

  implicit val provideSessionInfoFormat = Json.format[ProvideSessionInfo]
}
