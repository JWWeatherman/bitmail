package model.models

import play.api.libs.functional.syntax._
import play.api.libs.json._
import forms.CreateWalletForm

/*
* Main wallet data to be stored during transaction
* @property transData {CreateWalletForm.Data} form data sent from sender
* @property seed {Seed} mnemonic and binary key
* @property privateKey {String} private key
* @property publicKey {String} public key
* */

case class SnailTransaction(transData: CreateWalletForm.Data, seed: Seed, privateKey: String, publicKey: String, publicKeyAddress: String)
object SnailTransaction {
  implicit val jsonFormat = Json.format[SnailTransaction]

  val walletReads: Reads[SnailTransaction] = (
  (JsPath \ "transData").read[CreateWalletForm.Data] and
  (JsPath \ "seed").read[Seed] and
  (JsPath \ "privateKey").read[String] and
  (JsPath \ "publicKey").read[String] and
  (JsPath \ "publicKeyAddress").read[String]
  )(SnailTransaction.apply _)

  val walletWrites: Writes[SnailTransaction] = (
  (JsPath \ "transData").write[CreateWalletForm.Data] and
  (JsPath \ "seed").write[Seed] and
  (JsPath \ "privateKey").write[String] and
  (JsPath \ "publicKey").write[String] and
  (JsPath \ "publicKeyAddress").write[String]
  )(unlift(SnailTransaction.unapply))

  implicit val walletFormat: Format[SnailTransaction] =
    Format(walletReads, walletWrites)
}