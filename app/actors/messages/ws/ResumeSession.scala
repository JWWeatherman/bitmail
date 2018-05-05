package actors.messages.ws

import dataentry.utility.SecureIdentifier
import play.api.libs.json.Json

// Sent by the client to resume a previous session
case class ResumeSession(sessionId: SecureIdentifier) extends WebSocketMessage {
  override def kind : Symbol = ResumeSession.kind
}

object ResumeSession {
  final val kind = 'resumeSession

  implicit val resumeSessionFormat = Json.format[ResumeSession]
}
