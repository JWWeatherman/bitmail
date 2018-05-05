package actors.messages.ws

import play.api.libs.json._

class RequestSessionInfo() extends WebSocketMessage {
  override def kind: Symbol = RequestSessionInfo.kind
}

object RequestSessionInfo {
  final val kind = 'requestSessionInfo

  def apply() : RequestSessionInfo = new RequestSessionInfo()

  implicit val requestSessionInfoFormat = new Format[RequestSessionInfo] {
    override def writes(o: RequestSessionInfo): JsValue =
      JsObject(Seq(("kind", JsString(RequestSessionInfo.kind.name))))

    override def reads(json: JsValue): JsResult[RequestSessionInfo] = JsSuccess(new RequestSessionInfo())
  }
}
