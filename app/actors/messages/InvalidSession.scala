package actors.messages

import dataentry.utility.SecureIdentifier

// Returned by the session manager when a resumed session id is invalid
case class InvalidSession(sessionId : SecureIdentifier)
