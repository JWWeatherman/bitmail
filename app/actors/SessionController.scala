package actors

import java.security.SecureRandom
import java.util.Base64

import actors.messages._
import actors.messages.ws.{ ProvideSessionInfo, RequestSessionInfo, ResumeSession }
import akka.actor.Actor
import akka.pattern.pipe
import com.google.inject.Inject
import com.google.inject.name.Named
import dataentry.utility.SecureIdentifier
import model.SessionStorage
import model.models.SessionInfo
import play.api.libs.json._

@Named(ActorNames.SessionController)
class SessionController @Inject() (sessionStorage : SessionStorage) extends Actor {

  val sr = new SecureRandom()

  import context.dispatcher


  override def receive : Receive = {
    case msg : RequestSessionInfo =>
      val id = SecureIdentifier(64)
      sessionStorage.insertSession(SessionInfo(id))
      sender() ! ProvideSessionInfo(sessionId = id)
    case msg : ResumeSession =>
      (for {
        session <- sessionStorage.lookupSession(msg.sessionId)
      } yield {
        session.map(si => ProvideSessionInfo(si.sessionId)).getOrElse(InvalidSession(msg.sessionId))
      }).pipeTo(sender())
  }
}
