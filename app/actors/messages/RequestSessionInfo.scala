package actors.messages

import play.api.libs.json.Json

case class RequestSessionInfo(kind : String = RequestSessionInfo.kind.name) extends WebSocketMessage

object RequestSessionInfo {
  final val kind = 'requestSessionInfo

  implicit val requestSessionInfoFormat = Json.format[RequestSessionInfo]
}
