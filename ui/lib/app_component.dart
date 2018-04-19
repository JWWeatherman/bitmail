import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';

import 'src/home/home.dart';
import 'src/sender/sender.dart';

// AngularDart info: https://webdev.dartlang.org/angular
// Components info: https://webdev.dartlang.org/components

@Component(
  selector: 'my-app',
  styleUrls: const ['app_component.css'],
  templateUrl: 'app_component.html',
  directives: const [materialDirectives, ROUTER_DIRECTIVES],
  providers: const [
    materialProviders
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
class AppComponent {
  // Nothing here yet. All logic is in TodoListComponent.
}
