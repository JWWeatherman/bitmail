package actors.messages.ws

import play.api.libs.json.{ JsObject, JsString, JsValue, Json }

// Translates web socket json to and from WebSocketMessage classes
class JsonMarshaller {

  import ProvideSessionInfo._
  import RequestSessionInfo._
  import ResumeSession._
  import SendRequest._

  def deserialize(jsValue: JsValue): WebSocketMessage = {
    (for {
      jsObject <- jsValue.asOpt[JsObject]
      kind <- jsObject.value.get("kind").flatMap(_.asOpt[String].map(Symbol(_)))
    } yield kind match {
      case ProvideSessionInfo.kind => jsValue.asOpt[ProvideSessionInfo]
      case RequestSessionInfo.kind => jsValue.asOpt[RequestSessionInfo]
      case ResumeSession.kind => jsValue.asOpt[ResumeSession]
      case SendRequest.kind =>
        Some(jsValue.as[SendRequest])
    }).flatten.getOrElse(UnknownMessage(jsValue))
  }

  def serialize(wsm : WebSocketMessage) : JsValue = {
    wsm match {
      case m : ProvideSessionInfo => Json.toJson(m).asInstanceOf[JsObject] + (WebSocketMessage.kindField, JsString(ProvideSessionInfo.kind.name))
      case m : RequestSessionInfo => Json.toJson(m).asInstanceOf[JsObject] + (WebSocketMessage.kindField, JsString(RequestSessionInfo.kind.name))
      case m : ResumeSession => Json.toJson(m).asInstanceOf[JsObject] + (WebSocketMessage.kindField, JsString(ResumeSession.kind.name))
      case m : SendRequest => Json.toJson(m).asInstanceOf[JsObject] + (WebSocketMessage.kindField, JsString(SendRequest.kind.name))
      case _ => JsObject(Seq(WebSocketMessage.kindField -> JsString(UnknownMessage.kind.name)))
    }
  }
}
