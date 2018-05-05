package model.models

import dataentry.utility.SecureIdentifier

case class SessionInfo(sessionId : SecureIdentifier)

object SessionInfo {
  final val sessionIdField : String = "sessionId"
}