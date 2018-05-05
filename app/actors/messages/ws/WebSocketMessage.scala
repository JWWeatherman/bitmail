package actors.messages.ws

trait WebSocketMessage {
  def kind : Symbol
}

object WebSocketMessage {
  final val kindField = "kind"
}
