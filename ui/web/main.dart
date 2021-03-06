import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';
import 'package:ui/app_component.dart';
import 'package:ui/src/services/Configuration.dart';
import 'package:ui/src/services/SocketManager.dart';

void main() {
  bootstrap(AppComponent, [
    ROUTER_PROVIDERS,
    Configuration,
    provide(SocketManager, useFactory: socketManagerFactory, deps: [Configuration]),

    // Remove next line in production
    provide(LocationStrategy, useClass: HashLocationStrategy),
  ]);
}
