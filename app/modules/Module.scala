package modules
import actors.BitcoinClientActor
import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

class Module extends AbstractModule with AkkaGuiceSupport {
  override def configure() = {
    bindActor[BitcoinClientActor]("BitcoinClientActor")
  }
}
