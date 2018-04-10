package model

import com.google.inject.{Inject, Singleton}
import forms.Data
import model.models.{Seed, SnailWallet}
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.bson.codecs.{DEFAULT_CODEC_REGISTRY, Macros}
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.IndexOptions
import org.mongodb.scala.model.Indexes._
import org.mongodb.scala.model.Updates._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class WalletStorage @Inject()(bitmailDb: MongoDatabase)(implicit ec: ExecutionContext) {

  case class BounceSearchable(wallet: SnailWallet, sender: Option[String], recipient: String, bounced: Boolean)

  object BounceSearchable {

    final val walletField = "wallet"
    final val senderField = "sender"
    final val recipientField = "recipient"
    final val bouncedField = "bounced"

    def apply(wallet: SnailWallet): BounceSearchable = {
      BounceSearchable(wallet,
                       wallet.transData.senderEmail.map(_.toLowerCase),
                       wallet.transData.recipientEmail.toLowerCase,
                       bounced = false)
    }
  }

  val codecRegistry = fromRegistries(
    fromProviders(
      Macros.createCodecProvider[Data],
      Macros.createCodecProvider[SnailWallet],
      Macros.createCodecProvider[Seed],
      Macros.createCodecProvider[BounceSearchable]()
    ),
    DEFAULT_CODEC_REGISTRY
  )
  final val collectionLabel = "wallets"

  val collection = bitmailDb.getCollection[BounceSearchable](collectionLabel).withCodecRegistry(codecRegistry)

  def insertWallet(wallet: SnailWallet) =
    collection.insertOne(BounceSearchable(wallet)).toFutureOption()

  def deleteWallet(publicKey: String) =
    collection
      .deleteOne(equal(s"${BounceSearchable.walletField}.${SnailWallet.publicKeyField}", publicKey))
      .toFutureOption() onComplete {
      case Success(_) => true
      case Failure(_) => false // Needs logging
    }

  def findWallet(publicKey: String) =
    collection
      .find(equal(s"${BounceSearchable.walletField}.${SnailWallet.publicKeyField}", publicKey))
      .toFuture()
      .map(c => c.map(_.wallet).headOption)

  def findAllWallets = collection.find().toFuture().map(c => c.map(_.wallet))

  for {
    recipientIndexCreated <- bitmailDb
      .getCollection(collectionLabel)
      .createIndex(ascending(s"recipient"), new IndexOptions().name("recipientEmailIndex"))
      .toFutureOption()
    senderIndexCreated <- bitmailDb
      .getCollection(collectionLabel)
      .createIndex(ascending(s"sender"), new IndexOptions().name("senderEmailIndex"))
      .toFutureOption()
  } yield {
    (recipientIndexCreated, senderIndexCreated)
    // Needs a logging statement to report success or fail of index creation
  }

  def findUnbouncedWalletsByEmail(email: String) =
    collection.find(and(equal(BounceSearchable.recipientField, email),equal(BounceSearchable.bouncedField,false))).toFuture().map(v => v.map(_.wallet))

  def markWalletBounced(wallet: SnailWallet) =
    collection
      .updateOne(equal(s"${BounceSearchable.walletField}.${SnailWallet.publicKeyAddressField}",
                       wallet.publicKeyAddress),
                 set(s"${BounceSearchable.bouncedField}", true))
      .toFutureOption()

}
