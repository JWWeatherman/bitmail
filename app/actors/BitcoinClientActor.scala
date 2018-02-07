package actors
import java.io.File
import java.nio.file.{ Path, Paths }
import java.security.spec.ECPoint
import java.util

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.google.inject.name.Named
import org.bitcoinj.core.listeners.PeerDataEventListener
import org.bitcoinj.core._
import org.bitcoinj.net.discovery.DnsDiscovery
import org.bitcoinj.params.TestNet3Params
import org.bitcoinj.store.{ H2FullPrunedBlockStore, MemoryBlockStore }
import org.bitcoinj.wallet.Wallet
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener
import org.spongycastle.util.encoders.Hex

import scala.collection.JavaConversions._


@Named("BitcoinClientActor")
class BitcoinClientActor extends Actor {

  val networkParams = TestNet3Params.get()
  val peerGroupContext = new Context(networkParams)
  val tablePath = Paths.get(".").resolve("BlockChainDB").normalize().toAbsolutePath.toString
  val blockStore = new H2FullPrunedBlockStore(networkParams, tablePath, 1000)
  val blockChain = new BlockChain(peerGroupContext, blockStore )
  val peerGroup = new PeerGroup(peerGroupContext, blockChain)

  class PeerDataListenerImpl extends PeerDataEventListener {
    override def onPreMessageReceived(peer : Peer, m : Message) : Message = m

    override def onChainDownloadStarted(peer : Peer, blocksLeft : Int)  = {
    }

    override def getData(peer : Peer, m : GetDataMessage) : util.List[Message] = {
      null
    }

    var blockCount = 0
    override def onBlocksDownloaded(peer : Peer, block : Block, filteredBlock : FilteredBlock, blocksLeft : Int)  = {
      println("BLOCK COUNT: " + blockCount)
      println("BLOCKS LEFT: " + blocksLeft)
      blockCount += 1
    }
  }

  val publicKey = "048a0cf9c7df81268380375c5c1788f1d309bae3d739d382d9e53275b057a6e6fac3d7bfb7bb2d8b7001e995ca617eb485a67378f3f1d35f517b2eab39f8042ac5"
  val publicKeyBytes = Hex.decode(publicKey)
  val walletExperiment = Wallet.fromKeys(networkParams, Seq(ECKey.fromPublicOnly(publicKeyBytes)))

  val listener  = new PeerDataListenerImpl()

  val coinsReceivedEventListener = new WalletCoinsReceivedEventListener {
    override def onCoinsReceived(wallet : Wallet, tx : Transaction, prevBalance : Coin, newBalance : Coin) : Unit = {
      val bool = true
      val file = new File("./coinsReceived.txt")
      file.createNewFile()
    }
  }

  override def preStart() : Unit = {
    super.preStart()
    peerGroup.setUserAgent("BitMail", "0.0")
    peerGroup.addPeerDiscovery(new DnsDiscovery(networkParams) )
    peerGroup.addWallet(walletExperiment)
    walletExperiment.addCoinsReceivedEventListener(coinsReceivedEventListener)
    peerGroup.startAsync()
    peerGroup.startBlockChainDownload(listener)
  }

  override def receive : Receive = {
    case x : String =>
  }
}
