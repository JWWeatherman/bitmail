package loggers

import actors.messages.LoadAllWallets
import com.google.inject.Singleton
import model.models.SnailWallet
import org.bitcoinj.wallet.Wallet
import play.api.Logger

@Singleton
class BitSnailLogger {
  def UnableToUpdateTransation(transactionId : String, error: String) = logger.error(s"Mongo could not update transaction. transactionId = $transactionId; mongoError = $error")

  def MissingTransaction(transactionId : String) = logger.error(s"Transaction not found. transactionId = $transactionId")

  def KickingWalletWatcher() = logger.info("Kick stating the wallet watcher.")

  def LoadingAllWallets(previousWallets : LoadAllWallets) = logger.info(s"LoadAllWallets previousWallets.count=${previousWallets.wallets.length}")

  def StartingBitcoinNetwork(network : String) = logger.info(s"Starting bitmail snail.  network=$network")

  def StartedWatchingWallet(wallet : SnailWallet)  = logger.info(s"Started watching wallet.publicKey=${wallet.publicKey}")

  def FinishedBlockDownload() = logger.info("Finished blockchain download")

  def BlockChainDownloadStarted(blocksLeft : Int) = logger.info(s"Started blockchain download. blockCount=$blocksLeft")

  def MissingWallet(wallet : Wallet) = logger.error(s"A watched wallet could not be found in mongo database.  bitcoinjWallet=${wallet.toString(false, true, false, null)}")

  def NewTransaction(walletContext : SnailWallet, transactionId : String) = logger.info(s"New transaction for wallet.publicKey=${walletContext.publicKey} transaction.id = ${transactionId}")


  def SeenTransaction(transactionId : String) = logger.info(s"Previously handled transaction seen. TransactionId = $transactionId")

  private val logger = Logger("bitsnail")

}
