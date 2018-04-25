package actors.messages

import play.api.libs.json.Json

case class ProvideSessionInfo(kind : String = ProvideSessionInfo.kind.name, sessionId: String) extends WebSocketMessage

object ProvideSessionInfo {
  final val kind = 'provideSessionInfo

  implicit val provideSessionInfoFormat = Json.format[ProvideSessionInfo]
}
