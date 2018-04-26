import 'package:angular/angular.dart';
import 'package:ui/src/services/SocketManager.dart';
import 'package:ui/src/messages/SocketMessages.dart';
import 'package:angular_router/angular_router.dart';
import 'dart:html';

@Injectable()
class SessionController {
  SocketManager _socketManager;
  Router _router;
  String sessionId;

  SessionController(this._socketManager, this._router) {
    listenForSessionInfo();
  }

  bool HasData() {
    return sessionId != null;
  }

  void StartOrResumeSession() {
    var si = window.localStorage["sessionId"];
    if (si != null) {
      sessionId = si;
      _socketManager.sendToServer(SocketMessageHelper.ResumeSession(si));
      print("Resume session ${sessionId}");
    } else {
      _socketManager.sendToServer(SocketMessageHelper.RequestSessionInfo());
    }
  }

  void listenForSessionInfo() {
    var stream =
    _socketManager.getStream(SocketMessageKinds.provideSessionInfo);
    stream.listen((SocketMessage sm) {
      sessionId = sm.sessionId;
      window.localStorage["sessionId"] = sessionId;
      print("Session id ${sessionId}");
    });
  }
}
