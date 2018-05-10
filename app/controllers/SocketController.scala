package controllers
import actors.messages.ws.{JsonMarshaller, WebSocketMessage}
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import com.google.inject.Inject
import play.api.libs.streams.ActorFlow
import play.api.mvc.{Controller, WebSocket}
import actors.{ActorNames, RequestGenerator, SocketManager}
import akka.stream.scaladsl.Flow
import com.google.inject.name.Named
import play.api.libs.json.JsValue

class SocketController @Inject()(
    @Named(ActorNames.SessionController) sessionController: ActorRef,
    @Named(ActorNames.RequestGenerator) requestGenerator: ActorRef,
    jsonMarshaller: JsonMarshaller
)(implicit system: ActorSystem, mat: Materializer)
    extends Controller {

  def socket = WebSocket.accept[JsValue, JsValue] { request =>
    Flow[JsValue]
      .map(js => {
        println(js.toString())
        js
      })
      .map(jsonMarshaller.deserialize)
      .via(ActorFlow.actorRef[WebSocketMessage, WebSocketMessage](out => SocketManager.props(out, sessionController, requestGenerator)))
      .map(wsm => jsonMarshaller.serialize(wsm))
  }
}
