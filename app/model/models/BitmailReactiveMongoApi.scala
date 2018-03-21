package model.models

import com.google.inject.Inject
import play.api.Configuration
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.gridfs.GridFS
import reactivemongo.api.{ DefaultDB, MongoConnection, MongoConnectionOptions, MongoDriver }
import reactivemongo.play.json.JSONSerializationPack

import scala.concurrent.{ ExecutionContext, Future }

class BitmailReactiveMongoApi @Inject() (
  configuration: Configuration
                                        )
                                        (implicit ec: ExecutionContext)
  extends ReactiveMongoApi {

  lazy val defaultMongoUri = MongoConnection.ParsedURI(
    List((MongoConnection.DefaultHost, MongoConnection.DefaultPort)), MongoConnectionOptions(), List.empty[String], Some("bitmail"), None
  )

  val mongoUri = configuration.getString("mongodb.uri").flatMap(v => MongoConnection.parseURI(v).toOption).getOrElse(defaultMongoUri)
  //mongodb://localhost:27017/bitmail

  lazy val internalDriver = new MongoDriver()
  lazy val internalconnection = internalDriver.connection(mongoUri)

  override def driver : MongoDriver = internalDriver

  override def connection : MongoConnection = internalconnection

  override def database : Future[DefaultDB] = for {
    db <- internalconnection.database(mongoUri.db.get)
  } yield db

  override def asyncGridFS : Future[GridFS[JSONSerializationPack.type]] = ???

  override def db : DefaultDB = ???

  override def gridFS : GridFS[JSONSerializationPack.type] = ???
}
