import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';

@Component(
  selector: 'todo-list',
  styleUrls: const ['home.css'],
  templateUrl: 'home.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
  ]
)
class Home implements OnInit {

  @override
  Future<Null> ngOnInit() async {
  }
}
