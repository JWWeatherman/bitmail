package model

import com.google.inject.Inject
import model.models.{ BitcoinTransaction, SessionInfo }
import org.bson.codecs.configuration.CodecRegistries.{ fromProviders, fromRegistries }
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.bson.codecs.{ DEFAULT_CODEC_REGISTRY, Macros }
import org.mongodb.scala.model.Filters._

import scala.concurrent.ExecutionContext

class SessionStorage @Inject()(bitmailDb : MongoDatabase)(implicit ec : ExecutionContext) {

  final val collectionLabel = "sessions"

  import BitcoinTransaction._

  val codecRegistry =
    fromRegistries(fromProviders(Macros.createCodecProvider[SessionInfo]()), DEFAULT_CODEC_REGISTRY)

  val collection = bitmailDb.getCollection[SessionInfo](collectionLabel).withCodecRegistry(codecRegistry)

  def insertSession(session: SessionInfo) = collection.insertOne(session).toFutureOption()

}
