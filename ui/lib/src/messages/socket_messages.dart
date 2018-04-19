import 'package:json_object/json_object.dart';
import 'dart:convert';

class SocketMessage extends JsonObject {
  String kind;

  SocketMessage({this.kind = "SocketMessage"});

  SocketMessage.withKind(String kind) {
    this.kind = kind;
  }

  // Moves JsonObject map into fields
  void Lift() {
    kind = this["kind"];
  }

  // Moves fields into JsonObject
  void Drop() {
    this["kind"] = kind;
  }
}

class SendRequest extends SocketMessage {
  String recipientEmail;
  String senderEmail;
  String senderMessage;
  bool remainAnonymous;

  SendRequest(this.recipientEmail, this.senderEmail, this.senderMessage,
      this.remainAnonymous) : super.withKind("SendRequest");

  SendRequest.newSender() : this("", "", "", false);

  @override
  void Lift() {
    super.Lift();
    recipientEmail = this["recipientEmail"];
    senderEmail = this["senderEmail"];
    senderMessage = this["senderMessage"];
    remainAnonymous = JSON.decode("remainAnonymous");
  }

  @override
  void Drop() {
    super.Drop();
    this["recipientEmail"] = recipientEmail;
    this["senderEmail"] = senderEmail;
    this["senderMessage"] = senderMessage;
    this["remainAnonymous"] = JSON.encode(remainAnonymous);
  }

}