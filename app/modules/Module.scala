package modules
import actors.{ BitcoinClientActor, NotificationSendingActor }
import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

class Module extends AbstractModule with AkkaGuiceSupport {
  override def configure() = {
    bindActor[BitcoinClientActor]("BitcoinClientActor")
    bindActor[NotificationSendingActor]("NotificationSendingActor")
  }
}
