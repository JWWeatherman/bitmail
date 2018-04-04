package model.models

import forms.Data
import play.api.libs.functional.syntax._
import play.api.libs.json._

/*
 * Main wallet data to be stored during transaction
 * @property transData {CreateWalletForm.Data} form data sent from sender
 * @property seed {Seed} mnemonic and binary key
 * @property privateKey {String} private key
 * @property publicKey {String} public key
 * */

case class SnailWallet(transData: Data, seed: Seed, privateKey: String, publicKey: String, publicKeyAddress: String)

object SnailWallet {

  final val transDataField = "transData"
  final val seedField = "seed"
  final val privateKeyField = "privateKey"
  final val publicKeyField = "publicKey"
  final val publicKeyAddressField = "publicKeyAddress"
  final val bouncedField = "bounced"

  implicit val jsonFormat = Json.format[SnailWallet]

  val walletReads: Reads[SnailWallet] = (
    (JsPath \ transDataField).read[Data] and
    (JsPath \ seedField).read[Seed] and
    (JsPath \ privateKeyField).read[String] and
    (JsPath \ publicKeyField).read[String] and
    (JsPath \ publicKeyAddressField).read[String]
  )(SnailWallet.apply _)

  val walletWrites: Writes[SnailWallet] = (
    (JsPath \ transDataField).write[Data] and
    (JsPath \ seedField).write[Seed] and
    (JsPath \ privateKeyField).write[String] and
    (JsPath \ publicKeyField).write[String] and
    (JsPath \ publicKeyAddressField).write[String]
  )(unlift(SnailWallet.unapply))

  implicit val walletFormat: Format[SnailWallet] =
    Format(walletReads, walletWrites)
}
