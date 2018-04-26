package actors

import java.security.SecureRandom
import java.util.Base64

import actors.messages._
import akka.actor.Actor
import akka.pattern.pipe
import com.google.inject.Inject
import com.google.inject.name.Named
import model.SessionStorage
import model.models.SessionInfo
import play.api.libs.json._

@Named(ActorNames.SessionController)
class SessionController @Inject() (sessionStorage : SessionStorage)  extends Actor {

  val sr = new SecureRandom()

  import context.dispatcher

  def createSessionId = {
    val bytes = Array.fill[Byte](64)(0)
    sr.nextBytes(bytes)
    Base64.getEncoder.encodeToString(bytes)
  }

  override def receive : Receive = {
    case msg : RequestSessionInfo =>
      val id = createSessionId
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
