import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:ui/src/services/SessionController.dart';
import 'package:ui/src/services/SocketManager.dart';
import 'package:ui/src/messages/SocketMessages.dart';
import 'src/home/home.dart';
import 'src/sender/sender.dart';

@Component(
  selector: 'bit-snail',
  styleUrls: const ['app_component.css'],
  templateUrl: 'app_component.html',
  directives: const [materialDirectives, ROUTER_DIRECTIVES],
  providers: const [
    materialProviders,
    SessionController,
  ],
)
@RouteConfig(const [
  const Route(
    path: '/',
    name: 'Home',
    component: Home,
    useAsDefault: true,
  ),
  const Route(path: '/sender', name: 'Sender', component: SenderFormComponent),
  const Redirect(path: '/**', redirectTo: const ['Home'])
])
class AppComponent extends OnInit {

  SessionController sessionController;
  AppComponent(this.sessionController);


  @override
  ngOnInit() {
    sessionController.StartOrResumeSession();
  }
}
