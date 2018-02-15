package bitcoin

import actors.messages.{ InitiateBlockChain, LoadAllWallets }
import akka.actor.ActorRef
import com.google.inject.Inject
import com.google.inject.name.Named
import forms.CreateWalletForm
import model.models.SnailWallet
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.Cursor
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext

class StartupWalletLoader @Inject()(
  mongoApi : ReactiveMongoApi,
  @Named("BitcoinClientActor") bitcoinClient : ActorRef)(implicit ec: ExecutionContext)
{
  def initiateBlockChain = {
    bitcoinClient ! InitiateBlockChain()
  }


  def loadAllWallets = {
    for {
      database <- mongoApi.database
      transactions = database.collection[JSONCollection]("transactions")
      snails <- transactions.find(Json.obj())(transactions.pack.writer(a => a)).cursor[SnailWallet]().collect(-1, Cursor.ContOnError[List[SnailWallet]]((v, e) => {}))
    } yield {
      bitcoinClient ! LoadAllWallets(snails)
    }
  }

  initiateBlockChain
  loadAllWallets


}
