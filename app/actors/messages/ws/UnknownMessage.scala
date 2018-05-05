package actors.messages.ws

import play.api.libs.json.JsValue

case class UnknownMessage(json : JsValue) extends WebSocketMessage {
  override def kind : Symbol = UnknownMessage.kind
}

object UnknownMessage {
  final val kind = 'unknownMessage
}
