package model

import com.google.inject.Inject
import model.models.SnailWallet
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument

import scala.concurrent.ExecutionContext
import play.modules.reactivemongo.ReactiveMongoApi

class WalletStorage @Inject()(mongoApi: ReactiveMongoApi)(implicit ec: ExecutionContext) {

  val collectionLabel = "wallets"

  def insertWallet(wallet: SnailWallet) =
  {
    for {
      db <- mongoApi.database
      result <- db.collection[BSONCollection](collectionLabel).insert(wallet)
    } yield result
  }

  def deleteWallet(publicKey: String) = {
    for {
      db <- mongoApi.database
      result <- db.collection[BSONCollection](collectionLabel).remove(BSONDocument(SnailWallet.publicKeyField -> publicKey))
    } yield result
  }
}
