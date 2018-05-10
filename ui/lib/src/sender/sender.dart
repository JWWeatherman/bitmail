import 'package:angular/angular.dart';
import 'package:angular_forms/angular_forms.dart';

import 'package:ui/src/messages/SocketMessages.dart';
import 'package:ui/src/services/SocketManager.dart';
import 'package:ui/src/services/SessionController.dart';

class SendRequest extends SocketMessage {
  String recipientEmail;
  String senderEmail;
  String senderMessage;
  String remainAnonymous;
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
  SessionController sessionController;

  SenderFormComponent(this.socket, this.sessionController) {
    model = new SendRequest();
  }

  void onSubmit() {
    var message = SocketMessageHelper.SendRequest(this.sessionController.sessionId, model.recipientEmail, model.senderEmail, model.senderMessage, model.remainAnonymous == null ? false : model.remainAnonymous);
    socket.sendToServer(message);
  }

}