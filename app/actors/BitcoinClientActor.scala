package actors
import java.io.File
import java.nio.file.{ Path, Paths }
import java.util

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.google.inject.name.Named
import org.bitcoinj.core.listeners.PeerDataEventListener
import org.bitcoinj.core._
import org.bitcoinj.net.discovery.DnsDiscovery
import org.bitcoinj.params.TestNet3Params
import org.bitcoinj.store.{ H2FullPrunedBlockStore, MemoryBlockStore }

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
      blockCount += 1
    }
  }

  val listener  = new PeerDataListenerImpl()

  override def preStart() : Unit = {
    super.preStart()
    peerGroup.setUserAgent("BitMail", "0.0")
    peerGroup.addPeerDiscovery(new DnsDiscovery(networkParams) )
    peerGroup.startAsync()
    peerGroup.startBlockChainDownload(listener)
  }

  override def receive : Receive = {
    case x : String =>
  }
}
