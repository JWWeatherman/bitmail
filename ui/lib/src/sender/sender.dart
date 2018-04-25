import 'package:angular/angular.dart';
import 'package:angular_forms/angular_forms.dart';

import 'package:ui/src/messages/SocketMessages.dart';
import 'package:ui/src/services/SocketManager.dart';

class SendRequest extends SocketMessage {
  String recipientEmail;
  String senderEmail;
  String senderMessage;
  bool remainAnonymous;
}

@Component(
  selector : 'sender-form',
  templateUrl : 'sender.html',
  styleUrls: const ['sender.css'],
  directives: const [CORE_DIRECTIVES, formDirectives],
)
class SenderFormComponent {

  SendRequest model;
  SocketManager socket;

  SenderFormComponent(this.socket) {
    model = new SendRequest();
  }

  void onSubmit() {
    var message = new SocketMessage();
    message.recipientEmail = model.recipientEmail;
    message.senderEmail = model.senderEmail;
    message.senderMessage = model.senderMessage;
    message.remainAnonymous = model.remainAnonymous;
    socket.sendToServer(message);
  }

}