package actors.messages

import play.api.libs.json.Json

// Sent by the client to resume a previous session
case class ResumeSession(sessionId: String, kind: String = ResumeSession.kind.name) extends WebSocketMessage

object ResumeSession {
  final val kind = 'resumeSession

  implicit val resumeSessionFormat = Json.format[ResumeSession]
}
