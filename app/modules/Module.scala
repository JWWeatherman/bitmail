package modules
import actors._
import bitcoin.StartupWalletLoader
import com.google.inject.{ AbstractModule, Provides }
import email.{ Email, EmailWithSendGrid, SendGridConfiguration }
import org.mongodb.scala.{ MongoClient, MongoDatabase }
import play.api.libs.concurrent.AkkaGuiceSupport

class Module extends AbstractModule with AkkaGuiceSupport {
  override def configure() = {
    bindActor[BitcoinClientActor]("BitcoinClientActor")
    bindActor[NotificationSendingActor]("NotificationSendingActor")
    bindActor[EmailCommunicationsActor](ActorNames.EmailCommunications)
    bindActor[SendgridActor](ActorNames.Mail)
    bindActor[SessionController](ActorNames.SessionController)
    bindActor[RequestGenerator](ActorNames.RequestGenerator)
    bind(classOf[StartupWalletLoader]).asEagerSingleton()
    bind(classOf[Email]).to(classOf[EmailWithSendGrid])

  }

  @Provides
  def provideSendGrid(): SendGridConfiguration = {
    val key = System.getProperty("SENDGRIDSECRET")
    SendGridConfiguration(key)
  }

  @Provides
  def provideMongoDatabase(): MongoDatabase = {
    // To directly connect to the default server localhost on port 27017
    val mongoClient: MongoClient = MongoClient()
    mongoClient.getDatabase("bitmail")
  }
}
