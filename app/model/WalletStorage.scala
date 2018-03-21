package model

import com.google.inject.{ Inject, Singleton }
import forms.CreateWalletForm
import model.models.SnailWallet
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.Cursor
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.indexes.{ Index, IndexType }
import reactivemongo.bson.BSONDocument

import scala.concurrent.{ ExecutionContext, Future }

@Singleton()
class WalletStorage @Inject()(mongoApi: ReactiveMongoApi)(implicit ec: ExecutionContext) {

  def toBSON(json : JsValue) : BSONDocument =
    reactivemongo.play.json.BSONFormats.BSONDocumentFormat.reads(json).get

  def toBSON[T](o: T)(implicit tjs: Writes[T]): BSONDocument =
    reactivemongo.play.json.BSONFormats.BSONDocumentFormat.reads(Json.toJson[T](o)).get


  case class MongoCollationOptions(locale : String, strength : Int)
  case class MongoOptions(collation: MongoCollationOptions)
  implicit val f3 = new Writes[(String, String)] {
    override def writes(o : (String, String)) : JsValue = {
      JsObject(Seq(o._1 -> JsString(o._2)))
    }
  }
  implicit val f1 = new Writes[(String, (String, String))] {
    override def writes(o : (String, (String, String))) : JsValue = {
      JsObject(Seq(o._1 -> Json.toJson(o._2)))
    }
  }
  implicit val f4 = new Writes[(String, Boolean)] {
    override def writes(o : (String, Boolean)) : JsValue = {
      JsObject(Seq(o._1 -> JsBoolean(o._2)))
    }
  }
  implicit val f2 = new Writes[(String, (String, Boolean))] {
    override def writes(o : (String, (String, Boolean))) : JsValue = {
      JsObject(Seq(o._1 -> Json.toJson(o._2)))
    }
  }
  implicit val f6 = new Writes[(String, Seq[(String, String)])] {
    override def writes(o : (String, Seq[(String, String)])) : JsValue = {
      JsObject(Seq((
        o._1,
        JsArray(o._2.map(v => Json.toJson(v)))))
      )
    }
  }
  implicit val f7 = Json.format[MongoCollationOptions]
  implicit val f8 = Json.format[MongoOptions]

  import CreateWalletForm.Data._
  import SnailWallet._

  val collectionLabel = "wallets"

  // Create case insensitive index for sender email.
  val senderIndex = Index(
    key = Seq(s"$transDataField.$senderEmailField" -> IndexType.Ascending),
    name = Some("senderEmailIndex"),
    unique = false,
    options = toBSON(MongoOptions(MongoCollationOptions("en_US", 1))) // https://docs.mongodb.com/manual/core/index-case-insensitive/
  )

  // Create case insensitive index for sender email.
  val recipientIndex = Index(
    key = Seq(s"$transDataField.$recipientEmailField" -> IndexType.Ascending),
    name = Some("recipientEmailIndex"),
    unique = false,
    options = toBSON(MongoOptions(MongoCollationOptions("en_US", 1))) // https://docs.mongodb.com/manual/core/index-case-insensitive/
  )

  // Ensure the email address is indexed as case insensitive
  val senderIndexCreated = for {
    db <- mongoApi.database
    indexes <- db.collection[BSONCollection](collectionLabel).indexesManager.list()
    result <- {
      if (!indexes.exists(i => i.name == senderIndex.name)) {
        val m = db.collection[BSONCollection](collectionLabel).indexesManager
        m.create(senderIndex).map(v => Some(v))
      } else {
        Future(None)
      }
    }
  } yield result // Acts as a placeholder for the future that can block queries until the index is created

  // Ensure the email address is indexed as case insensitive
  val recipientIndexCreated = for {
    db <- mongoApi.database
    indexes <- db.collection[BSONCollection](collectionLabel).indexesManager.list()
    result <- {
      if (!indexes.exists(i => i.name == recipientIndex.name)) {
        val m = db.collection[BSONCollection](collectionLabel).indexesManager
        m.create(recipientIndex).map(v => Some(v))
      } else {
        Future(None)
      }
    }
  } yield result // Acts as a placeholder for the future that can block queries until the index is created

  def insertWallet(wallet : SnailWallet) = {
    for {
      db <- mongoApi.database
      result <- db.collection[BSONCollection](collectionLabel).insert(wallet)
    } yield result
  }

  def deleteWallet(publicKey : String) = {
    for {
      db <- mongoApi.database
      result <- db.collection[BSONCollection](collectionLabel).remove(BSONDocument(SnailWallet.publicKeyField -> publicKey))
    } yield result
  }

  def findWallet(publicKey : String) = {
    for {
      db <- mongoApi.database
      result <- db.collection[BSONCollection](collectionLabel).find(BSONDocument(SnailWallet.publicKeyField -> BSONDocument("$eq" -> publicKey))).one[Option[SnailWallet]]
    } yield result.flatten
  }

  def findAllWallets = {
    for {
      db <- mongoApi.database
      result <- db.collection[BSONCollection](collectionLabel).find(BSONDocument()).cursor[SnailWallet]().collect(-1, Cursor.ContOnError[List[SnailWallet]]((list, exeption) => {}))
    } yield result
  }

  def findWalletByEmail(email : String) = {
    for {
      gate1 <- senderIndexCreated
      gate2 <- recipientIndexCreated
      db <- mongoApi.database
      result <- {
        val query = toBSON(
          "$or" -> List(s"$transDataField.$recipientEmailField" -> email, s"$transDataField.$senderEmailField" -> email)
        )
        db.collection[BSONCollection](collectionLabel).find(query).options().one[Option[SnailWallet]]
      }

    } yield result.flatten
  }

  def markWalletBounced(wallet : SnailWallet) = {
    val selector = toBSON(
      JsObject(
        Seq(
          SnailWallet.publicKeyAddressField -> JsObject(
            Seq("$eq" -> JsString(wallet.publicKeyAddress)
            )
          )
        )
      )
    )


    val t = toBSON(Json.toJson(SnailWallet.publicKeyAddressField -> ("$eq" -> wallet.publicKeyAddress)))

    val update = toBSON(
      JsObject(Seq(
        "$set" -> JsObject(Seq(
          SnailWallet.bouncedField -> JsBoolean(false)
        ))
      ))

    )


    val u = toBSON(Json.toJson("$set" -> (SnailWallet.bouncedField -> false)))

    for {
      db <- mongoApi.database
      result <- db.collection[BSONCollection](collectionLabel).update(t, update)
    } yield result
  }


}
