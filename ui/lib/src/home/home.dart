import 'dart:async';
import 'package:ui/src/services/SocketManager.dart';
import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:ui/src/messages/SocketMessages.dart';

@Component(
  selector: 'todo-list',
  styleUrls: const ['home.css'],
  templateUrl: 'home.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
  ]
)
class Home {

  SocketManager socketManager;
  Home(this.socketManager);

  void onSendClicked()
  {
    socketManager.sendToServer(new SocketMessage.withKind(SocketMessageKinds.requestSessionInfo));
  }
}
