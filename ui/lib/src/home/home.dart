import 'dart:async';
import 'dart:html';
import 'package:angular_router/angular_router.dart';
import 'package:ui/src/services/SocketManager.dart';
import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:ui/src/messages/SocketMessages.dart';

@Component(
  selector: 'home',
  styleUrls: const ['home.css'],
  templateUrl: 'home.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
  ]
)
class Home {

  Router router;

  Home(this.router);

  void onSendClicked()
  {
    router.navigate(["Sender"]);
  }
}
