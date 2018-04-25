import 'package:angular/angular.dart';
import 'package:ui/src/services/SocketManager.dart';
import 'package:ui/src/messages/SocketMessages.dart';
import 'package:angular_router/angular_router.dart';

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

  void listenForSessionInfo() {
    var stream =
        _socketManager.getStream(SocketMessageKinds.provideSessionInfo);
    stream.listen((SocketMessage sm) {
      sessionId = sm.sessionId;
      this._router.navigate(["Home"]);
    });
  }
}
