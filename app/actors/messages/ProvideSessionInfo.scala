package actors.messages

import play.api.libs.json.Json

// Sent to the client to provide new or existing session information
case class ProvideSessionInfo(sessionId: String, kind : String = ProvideSessionInfo.kind.name) extends WebSocketMessage

object ProvideSessionInfo {
  final val kind = 'provideSessionInfo

  implicit val provideSessionInfoFormat = Json.format[ProvideSessionInfo]
}
