import 'package:json_object_lite/json_object_lite.dart';

@proxy
class SocketMessage extends JsonObjectLite
{
  SocketMessage();

  factory SocketMessage.withKind(String kind)
  {
    var sm = new SocketMessage();
    sm["kind"] = kind;
    return sm;
  }

  factory SocketMessage.fromJsonString(String json)
  {
    return new JsonObjectLite.fromJsonString(json, new SocketMessage());
  }

  factory SocketMessage.fromJsonObject(JsonObjectLite jsonObject) {
    return JsonObjectLite.toTypedJsonObjectLite(
        jsonObject, new SocketMessage());
  }

  String Kind() {
    return this["kind"];
  }
}

class SocketMessageKinds
{
  static const String provideSessionInfo = "provideSessionInfo";
  static const String requestSessionInfo = "requestSessionInfo";
  static const String resumeSession = "resumeSession";
  static const String sendRequest = "sendRequest";
  static const String socketOpened = "socketOpened";
}

class SocketMessageHelper
{
  static SocketMessage ResumeSession(String sessionId)
  {
    var sm = new SocketMessage.withKind(SocketMessageKinds.resumeSession);
    sm["sessionId"] = sessionId;
    return sm;
  }

  static SocketMessage RequestSessionInfo() {
    return new SocketMessage.withKind(SocketMessageKinds.requestSessionInfo);
  }

  static SocketMessage SendRequest(String sessionId, String recipientEmail, String senderEmail, String senderMessage, bool remainAnonymous) {
    var sm = new SocketMessage.withKind(SocketMessageKinds.sendRequest);
    sm["sessionId"] = sessionId;
    sm["recipientEmail"] = recipientEmail;
    sm["senderEmail"] = senderEmail;
    sm["senderMessage"] = senderMessage;
    sm["remainAnonymous"] = remainAnonymous;
    return sm;
  }

  static SocketMessage SocketOpened() {
    return new SocketMessage.withKind(SocketMessageKinds.socketOpened);
  }


}