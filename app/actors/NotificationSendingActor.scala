package actors

import java.io.File

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.google.inject.name.Named
import messages.BitcoinTransactionReceived

@Named("NotificationSendingActor")
class NotificationSendingActor extends Actor {
   override def receive : Receive = {
    case bitcoinReceived : BitcoinTransactionReceived =>
      val file = new File(s"./coinsReceived-${bitcoinReceived.transData.recipientEmail}-${bitcoinReceived.newValue.value - bitcoinReceived.previousValue.value}.txt")
      file.createNewFile()
  }
}
