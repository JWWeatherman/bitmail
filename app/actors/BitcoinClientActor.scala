package actors
import java.io.File
import java.net.InetAddress
import java.nio.file.{ Path, Paths }
import java.security.spec.ECPoint
import java.util

import akka.actor.{ Actor, ActorRef }
import akka.actor.Actor.Receive
import bitcoin.WalletMaker
import com.google.inject.Inject
import com.google.inject.name.Named
import messages.{ BitcoinTransactionReceived, InitiateBlockChain, LoadAllWallets }
import model.models.SnailTransaction
import org.bitcoinj.core.listeners.PeerDataEventListener
import org.bitcoinj.core._
import org.bitcoinj.net.discovery.DnsDiscovery
import org.bitcoinj.params.{ RegTestParams, TestNet3Params }
import org.bitcoinj.store.{ H2FullPrunedBlockStore, MemoryBlockStore }
import org.bitcoinj.wallet.Wallet
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener
import org.spongycastle.util.encoders.Hex
import play.Configuration
import reactivemongo.play.json.collection.JSONCollection
import play.api.libs.json._
import play.modules.reactivemongo._

import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext


@Named("BitcoinClientActor")
class BitcoinClientActor @Inject()(
                                    mongoApi : ReactiveMongoApi,
                                    config : Configuration,
                                    @Named("NotificationSendingActor") notificationSendingActor : ActorRef)(implicit ec: ExecutionContext) extends Actor {

  val bitcoinNetwork = config.getString("bitsnail.bitcoin.network")
  val networkParams = bitcoinNetwork match {
    case "regtest" => RegTestParams.get()
    case "testnet" => TestNet3Params.get()
  }
  val peerGroupContext = new Context(networkParams)
  val tablePath = Paths.get(".").resolve("BlockChainDB").normalize().toAbsolutePath.toString
  val blockStore = bitcoinNetwork match {
    case "regtest" => new MemoryBlockStore(networkParams)
    case "testnet" => new H2FullPrunedBlockStore(networkParams, tablePath, 1000)
  }
  val blockChain = new BlockChain(peerGroupContext, blockStore )
  var peerGroup : PeerGroup = new PeerGroup(peerGroupContext, blockChain)




  val coinsReceivedEventListener = new WalletCoinsReceivedEventListener {
    override def onCoinsReceived(wallet : Wallet, tx : Transaction, prevBalance : Coin, newBalance : Coin) : Unit = {
      for {
        database <- mongoApi.database
        transactions = database.collection[JSONCollection]("transactions")
        walletContext <- {
          val publicKey = Hex.toHexString(wallet.getImportedKeys.get(0).getPubKey)
          transactions.find(Json.obj("publicKey" ->publicKey))(transactions.pack.writer(a => a)).one[SnailTransaction]
        }
      } yield {
        walletContext match {
          case Some(t) =>
            notificationSendingActor ! BitcoinTransactionReceived(t.transData, t.publicKeyAddress, prevBalance, newBalance)
          case None =>
            val i = 0
            // We were watching a wallet that we forgot about?
        }
      }
    }
  }

  val blockChainDownloadListener = new PeerDataEventListener {
    override def getData(peer : Peer, m : GetDataMessage) : util.List[Message] = null

    override def onChainDownloadStarted(peer : Peer, blocksLeft : Int) : Unit = {
      println("BLOCKCHAIN DOWNLOAD STARTED....")
    }

    override def onPreMessageReceived(peer : Peer, m : Message) : Message = m

    override def onBlocksDownloaded(peer : Peer, block : Block, filteredBlock : FilteredBlock, blocksLeft : Int) : Unit = {
      println("BLOCKS LEFT: " + blocksLeft)
    }
  }

  def addWallet(wallet : SnailTransaction): Unit = {
    val jWallet = Wallet.fromKeys(networkParams, Seq(ECKey.fromPublicOnly(Hex.decode(wallet.publicKey))))
    jWallet.setDescription(wallet.transData.recipientEmail)
    jWallet.addCoinsReceivedEventListener(coinsReceivedEventListener)
    jWallet.setAcceptRiskyTransactions(true)
    peerGroup.addWallet(jWallet)
  }

  override def receive : Receive = {
    case wallet : model.models.SnailTransaction =>
      Context.propagate(peerGroupContext)
      addWallet(wallet)

    case  init : InitiateBlockChain =>
      Context.propagate(peerGroupContext)
      peerGroup.setUserAgent("Bitcoin Mail Snail", "0.0")
      peerGroup.setStallThreshold(10000, 1)
      bitcoinNetwork match {
        case "regtest" =>
          peerGroup.addAddress(new PeerAddress(InetAddress.getLocalHost, 4001))
          peerGroup.addAddress(new PeerAddress(InetAddress.getLocalHost, 4002))
          peerGroup.addAddress(new PeerAddress(InetAddress.getLocalHost, 4003))
          peerGroup.addAddress(new PeerAddress(InetAddress.getLocalHost, 4004))
          peerGroup.addAddress(new PeerAddress(InetAddress.getLocalHost, 4005))
          peerGroup.addAddress(new PeerAddress(InetAddress.getLocalHost, 4006))
          peerGroup.addAddress(new PeerAddress(InetAddress.getLocalHost, 4007))
          peerGroup.addAddress(new PeerAddress(InetAddress.getLocalHost, 4008))
          peerGroup.addAddress(new PeerAddress(InetAddress.getLocalHost, 4009))
          peerGroup.addAddress(new PeerAddress(InetAddress.getLocalHost, 4010))
          peerGroup.start()
          peerGroup.connectToLocalHost()
        case "testnet" =>
          peerGroup.addPeerDiscovery(new DnsDiscovery(networkParams) )
          peerGroup.start()

      }

    case previousWallets : LoadAllWallets =>
      Context.propagate(peerGroupContext)
      for {
        w <- previousWallets.wallets
      } yield addWallet(w)
      peerGroup.startBlockChainDownload(blockChainDownloadListener)
  }

}