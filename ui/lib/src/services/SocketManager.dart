import 'package:angular/angular.dart';
import 'package:ui/src/services/Configuration.dart';
import 'dart:html';
import 'dart:collection';
import 'dart:async';
import 'package:ui/src/messages/SocketMessages.dart';
import 'dart:convert';

class MessageStreamBinding {
  StreamController<SocketMessage> _controller;
  Queue<SocketMessage> _preReceiveStreamQueue = new Queue<SocketMessage>();
  bool _hasListeners = false;

  void OnListen() {
    _hasListeners = true;
    while (_preReceiveStreamQueue.isNotEmpty) {
      _controller.add(_preReceiveStreamQueue.removeFirst());
    }
  }

  void OnCancel() {
    _hasListeners = false;
  }

  void Send(SocketMessage sm) {
    if (_hasListeners) {
      _controller.add(sm);
    } else {
      _preReceiveStreamQueue.add(sm);
    }
  }

  MessageStreamBinding() {
    _controller = new StreamController<SocketMessage>.broadcast(
        onListen: OnListen, onCancel: OnCancel);
  }
}

SocketManager socketManagerFactory(Configuration config) {
  var sm = new SocketManager(config);
  sm.connect();
  return sm;
}

@Injectable()
class SocketManager {
  Configuration _config;
  WebSocket _ws;
  bool _readyToSend = false;

  Queue<SocketMessage> _preSendStreamQueue = new Queue<SocketMessage>();

  Map<String, MessageStreamBinding> bindings = new Map.identity();

  SocketManager(this._config);

  void connect() {
    _ws = new WebSocket(_config.socketUri);
    _ws.onClose.listen((CloseEvent e) {
      _readyToSend = false;
      print("Web socket closed");
    });
    _ws.onMessage.listen((MessageEvent event) {
      dispatchOnKind(event.data);
    });
    _ws.onOpen.listen((Event e) {
      _readyToSend = true;
      while (_preSendStreamQueue.isNotEmpty) {
        this.sendToServer(_preSendStreamQueue.removeFirst());
      }
    });
  }

  MessageStreamBinding getOrCreateBinding(String kind) {
    var binding = bindings[kind];
    if (binding == null) {
      binding = new MessageStreamBinding();
      bindings[kind] = binding;
      print("Created new binding for ${kind}");
    }
    return binding;
  }

  void dispatchOnKind(dynamic data) {
    SocketMessage sm = new SocketMessage.fromJsonString(data.toString());
    getOrCreateBinding(sm.Kind()).Send(sm);
  }

  void sendToServer(SocketMessage sm) {
    if (_readyToSend)
      _ws.send(JSON.encode(sm));
    else
      _preSendStreamQueue.add(sm);
  }

  Stream<SocketMessage> getStream(String kind) {
    var binding = getOrCreateBinding(kind);
    return binding._controller.stream;
  }
}
