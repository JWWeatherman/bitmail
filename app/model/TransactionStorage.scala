package model

import com.google.inject.Inject
import model.models.BitcoinTransaction
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.bson.codecs.{DEFAULT_CODEC_REGISTRY, Macros}
import org.mongodb.scala.model.Filters._

import scala.concurrent.ExecutionContext

class TransactionStorage @Inject()(bitmailDb: MongoDatabase)(implicit ec: ExecutionContext) {

  final val collectionLabel = "transactions"

  import BitcoinTransaction._

  val codecRegistry =
    fromRegistries(fromProviders(Macros.createCodecProvider[BitcoinTransaction]()), DEFAULT_CODEC_REGISTRY)

  val collection = bitmailDb.getCollection[BitcoinTransaction](collectionLabel).withCodecRegistry(codecRegistry)

  def insertTransaction(transaction: BitcoinTransaction) = collection.insertOne(transaction).toFutureOption()

  def findTransactionByTransactionId(transactionId: String) =
    collection.find(equal(transactionIdField, transactionId)).first().toFutureOption()

  def findTransactionByPublicAddress(publicAddress: String) =
    collection.find(equal(publicAddressField, publicAddress)).first().toFutureOption()

  def replace(transaction: BitcoinTransaction) =
    collection.replaceOne(equal(transactionIdField, transaction.transactionId), transaction).toFutureOption()

}
