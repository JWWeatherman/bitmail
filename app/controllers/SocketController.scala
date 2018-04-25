package controllers
import akka.actor.{ ActorRef, ActorSystem }
import akka.stream.Materializer
import com.google.inject.Inject
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc.{ Controller, WebSocket }
import actors.{ ActorNames, SocketManager }
import com.google.inject.name.Named

class SocketController @Inject()(
                                @Named(ActorNames.SessionController) sessionController : ActorRef
                                )(implicit system: ActorSystem, mat: Materializer) extends Controller {

  def socket = WebSocket.accept[JsValue, JsValue] { request =>
    ActorFlow.actorRef { out =>
      SocketManager.props(out, sessionController)
    }
  }
}
