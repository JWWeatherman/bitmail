package modules
import actors._
import bitcoin.StartupWalletLoader
import com.google.inject.{ AbstractModule, Provides }
import email.{ Email, EmailWithMailer, EmailWithSendGrid, SendGridConfiguration }
import model.WalletStorage
import model.models.BitmailReactiveMongoApi
import play.api.libs.concurrent.AkkaGuiceSupport
import play.modules.reactivemongo.ReactiveMongoApi

class Module extends AbstractModule with AkkaGuiceSupport {
  override def configure() = {
    bindActor[BitcoinClientActor]("BitcoinClientActor")
    bindActor[NotificationSendingActor]("NotificationSendingActor")
    bindActor[EmailCommunicationsActor](ActorNames.EmailCommunications)
    bindActor[SendgridActor](ActorNames.Mail)
    bind(classOf[StartupWalletLoader]).asEagerSingleton()
    bind(classOf[Email]).to(classOf[EmailWithSendGrid])
    bind(classOf[ReactiveMongoApi]).to(classOf[BitmailReactiveMongoApi])
  }

  @Provides
  def provideSendGrid() : SendGridConfiguration = {
    val key = System.getProperty("SENDGRIDSECRET")
    SendGridConfiguration(key)
  }
}
