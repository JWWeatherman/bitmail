package model.models

import play.api.libs.functional.syntax._
import play.api.libs.json._
import forms.CreateWalletForm
import reactivemongo.bson.{ BSONDocument, BSONDocumentWriter }

/*
* Main wallet data to be stored during transaction
* @property transData {CreateWalletForm.Data} form data sent from sender
* @property seed {Seed} mnemonic and binary key
* @property privateKey {String} private key
* @property publicKey {String} public key
* */

case class SnailWallet(transData: CreateWalletForm.Data, seed: Seed, privateKey: String, publicKey: String, publicKeyAddress: String)

object SnailWallet {

  val transDataField = "transData"
  val seedField = "seed"
  val privateKeyField = "privateKey"
  val publicKeyField = "publicKey"
  val publicKeyAddressField = "publicKeyAddress"

  implicit val jsonFormat = Json.format[SnailWallet]

  val walletReads: Reads[SnailWallet] = (
  (JsPath \ "transData").read[CreateWalletForm.Data] and
  (JsPath \ "seed").read[Seed] and
  (JsPath \ "privateKey").read[String] and
  (JsPath \ "publicKey").read[String] and
  (JsPath \ "publicKeyAddress").read[String]
  )(SnailWallet.apply _)

  val walletWrites: Writes[SnailWallet] = (
  (JsPath \ "transData").write[CreateWalletForm.Data] and
  (JsPath \ "seed").write[Seed] and
  (JsPath \ "privateKey").write[String] and
  (JsPath \ "publicKey").write[String] and
  (JsPath \ "publicKeyAddress").write[String]
  )(unlift(SnailWallet.unapply))

  implicit val walletFormat: Format[SnailWallet] =
    Format(walletReads, walletWrites)

  implicit object SnailWalletWriter extends BSONDocumentWriter[SnailWallet] {

    override def write(t : SnailWallet) : BSONDocument = BSONDocument(
      transDataField -> t.transData,
      seedField -> t.seed,
      privateKeyField -> t.privateKey,
      publicKeyField -> t.publicKey,
      publicKeyAddressField -> t.publicKeyAddress
    )
  }
}