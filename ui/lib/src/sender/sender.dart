import 'package:angular/angular.dart';
import 'package:angular_forms/angular_forms.dart';

import 'package:ui/src/messages/socket_messages.dart';
import 'package:ui/src/services/socket_manager.dart';

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
    model = new SendRequest.newSender();
  }

  void onSubmit() {
    socket.send(model);
  }

}