package actors

import actors.messages.{EmailBounceCheck, EmailBounceNotification, MailSent}
import akka.actor.{Actor, ActorRef}
import com.google.inject.Inject
import com.google.inject.name.Named
import email.BounceNotificationEmailMessage
import forms.Data
import loggers.BitSnailLogger
import model.WalletStorage
import model.models.SnailWallet

import scala.concurrent.ExecutionContext

class EmailCommunicationsActor @Inject()(
    walletStorage: WalletStorage,
    logger: BitSnailLogger,
    @Named(ActorNames.Mail) mailActor: ActorRef
)(implicit ec: ExecutionContext)
    extends Actor {
  override def receive: Receive = {
    case bounceCheck: EmailBounceCheck =>
      mailActor ! bounceCheck

    case bounces: EmailBounceNotification =>
      for {
        record <- bounces.records
      } yield {
        (for {
          wallets <- walletStorage.findWalletByEmail(record.email)
        } yield {
          wallets.foreach {
            case wallet @ SnailWallet(Data(recipientEmail, Some(senderEmail), _, _), _, _, _, _) =>
              mailActor ! BounceNotificationEmailMessage(wallet.transData.recipientEmail, senderEmail, wallet)
            case wallet @ SnailWallet(Data(recipientEmail, None, _, _), _, _, _, _) =>
              logger.MailBouncedWithAnonymousSender(wallet.transData.recipientEmail)
            case _ =>
              var f = 0

          }
        }) onFailure {
          case f => f // Just a place for a breakpoint during development, should maybe be a log statement
        }
      }

    case mailSent: MailSent =>
      mailSent.sendMessage match {
        case BounceNotificationEmailMessage(_, _, wallet) =>
          for {
            result <- walletStorage.markWalletBounced(wallet)
          } yield {
            if (!result.exists(r => r.wasAcknowledged()))
              logger.CouldNotUpdateBounce(wallet)
          }
      }

  }

}
