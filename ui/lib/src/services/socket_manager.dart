import 'package:angular/angular.dart';
import 'package:ui/src/services/configuration.dart';
import 'dart:html';
import 'dart:collection';
import 'package:json_object/json_object.dart';
import 'dart:async';
import 'package:ui/src/messages/socket_messages.dart';
import 'dart:convert';


abstract class MessageStreamBindings {
  String kind;
  StreamController<SocketMessage> stream;
}

SocketManager socketManagerFactory(Configuration config) {
  var sm = new SocketManager(config);
  sm.connect();
  return sm;
}

@Injectable()
class SocketManager {

  Configuration config;
  WebSocket ws;
  Queue<SocketMessage> buffer = new Queue<SocketMessage>();

  Map<String, MessageStreamBindings> bindings = new Map.identity();

  SocketManager(this.config);

  void connect() {
    ws = new WebSocket(config.socketUri);
    ws.onMessage.listen((MessageEvent event) {
      dispatchOnKind(event.data);
    });
  }

  void dispatchOnKind(dynamic data) {
    SocketMessage sm = new JsonObject.fromJsonString(
        data.toString(), new SocketMessage());
    sm.Lift();
    var binding = bindings[sm.kind];
    if (binding != null) {
      binding.stream.add(data);
    } else {
       buffer.add(sm);
    }
  }

  void registerStream(MessageStreamBindings msm) {
    // Don't overwrite an existing mapping
    if (bindings.containsKey(msm.kind))
      return;

    // dispatch any early arrivals
    buffer.where((SocketMessage sm) => sm.kind == msm.kind).forEach((SocketMessage sm) => msm.stream.add(sm));
    bindings[msm.kind] = msm;
  }

  void send(SocketMessage sm) {
    sm.Drop();
    ws.send(JSON.encode(sm));
  }

}