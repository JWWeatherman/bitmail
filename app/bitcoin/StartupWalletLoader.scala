package bitcoin

import actors.messages.{ InitiateBlockChain, LoadAllWallets }
import akka.actor.ActorRef
import com.google.inject.Inject
import com.google.inject.name.Named
import model.WalletStorage

import scala.concurrent.ExecutionContext

class StartupWalletLoader @Inject()(
  walletStorage : WalletStorage,
  @Named("BitcoinClientActor") bitcoinClient : ActorRef)(implicit ec: ExecutionContext)
{
  def initiateBlockChain = {
    bitcoinClient ! InitiateBlockChain()
  }


  def loadAllWallets = {
    for {
      snails <- walletStorage.findAllWallets
    } yield {
      bitcoinClient ! LoadAllWallets(snails)
    }
  }

  initiateBlockChain
  loadAllWallets


}
