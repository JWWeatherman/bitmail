package model

import com.google.inject.Inject
import dataentry.utility.SecureIdentifier
import model.models.SessionInfo
import org.bson.codecs.configuration.CodecRegistries.{ fromProviders, fromRegistries }
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.bson.codecs.{ DEFAULT_CODEC_REGISTRY, Macros }
import org.mongodb.scala.model.Filters._

import scala.concurrent.ExecutionContext

class SessionStorage @Inject()(bitmailDb : MongoDatabase)(implicit ec : ExecutionContext) {


  final val collectionLabel = "sessions"

  val codecRegistry =
    fromRegistries(fromProviders(Macros.createCodecProvider[SessionInfo]()), DEFAULT_CODEC_REGISTRY)

  val collection = bitmailDb.getCollection[SessionInfo](collectionLabel).withCodecRegistry(codecRegistry)

  def insertSession(session: SessionInfo) = collection.insertOne(session).toFutureOption()

  def lookupSession(sessionId : SecureIdentifier) = collection.find(equal(SessionInfo.sessionIdField, sessionId)).toFuture().map(s => s.headOption)

}
