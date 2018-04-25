package actors

import java.security.SecureRandom
import java.util.Base64

import actors.messages.{ ProvideSessionInfo, RequestSessionInfo, RequestSessionInfoWithActor }
import akka.actor.Actor
import com.google.inject.Inject
import com.google.inject.name.Named
import model.SessionStorage
import model.models.SessionInfo

@Named(ActorNames.SessionController)
class SessionController @Inject() (sessionStorage : SessionStorage)  extends Actor {

  val sr = new SecureRandom()
  def createSessionId = {
    val bytes = Array.fill[Byte](64)(0)
    sr.nextBytes(bytes)
    Base64.getEncoder.encodeToString(bytes)
  }

  override def receive : Receive = {
    case msg : RequestSessionInfoWithActor =>
      val id = createSessionId
      sessionStorage.insertSession(SessionInfo(id))
      msg.actor ! ProvideSessionInfo(sessionId = id)
  }
}
