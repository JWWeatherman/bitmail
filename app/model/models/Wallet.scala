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

case class Wallet(transData: CreateWalletForm.Data, seed: Seed, privateKey: String, publicKey: String)
object Wallet {
  implicit val jsonFormat = Json.format[Wallet]

  val walletReads: Reads[Wallet] = (
  (JsPath \ "transData").read[CreateWalletForm.Data] and
  (JsPath \ "seed").read[Seed] and
  (JsPath \ "privateKey").read[String] and
  (JsPath \ "publicKey").read[String]
  )(Wallet.apply _)

  val walletWrites: Writes[Wallet] = (
  (JsPath \ "transData").write[CreateWalletForm.Data] and
  (JsPath \ "seed").write[Seed] and
  (JsPath \ "privateKey").write[String] and
  (JsPath \ "publicKey").write[String]
  )(unlift(Wallet.unapply))

  implicit val walletFormat: Format[Wallet] =
    Format(walletReads, walletWrites)
}