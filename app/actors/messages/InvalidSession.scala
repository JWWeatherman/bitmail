package actors.messages

// Returned by the session manager when a resumed session id is invalid
case class InvalidSession(sessionId : String)
