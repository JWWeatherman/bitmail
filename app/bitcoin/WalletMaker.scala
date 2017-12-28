package bitcoin

import play.mvc.Http
import fr.acinq.bitcoin._
import fr.acinq.bitcoin.Crypto._
import MnemonicCode._
import forms.CreateWalletForm.Data
import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath, Reads, Writes}

class WalletMaker extends Bitcoin {

  def genMnemonic(hexString: String): List[String] = toMnemonics(fromHexString(hexString))

  def genSeed(mnemonics: Seq[String]): BinaryData = toSeed(mnemonics, "TREZOR")

  def genWallet = {
    val hex: String = hexStringGen
    val mnemonic: List[String] = genMnemonic(hex)
    val binaryKey: BinaryData = genSeed(mnemonic)
    val privKey = privateKeyGen(binaryKey)
    val pubKey = privKey.publicKey

    Wallet(Seed(mnemonic, binaryKey.toString), privKey.toString, pubKey.toString)
  }
}

case class Wallet(seed: Seed, privateKey: String, publicKey: String)
object Wallet {
  val walletReads: Reads[Wallet] = (
  (JsPath \ "seed").read[Seed] and
  (JsPath \ "privateKey").read[String] and
  (JsPath \ "publicKey").read[String]
  )(Wallet.apply _)

  val walletWrites: Writes[Wallet] = (
  (JsPath \ "seed").write[Seed] and
  (JsPath \ "privateKey").write[String] and
  (JsPath \ "publicKey").write[String]
  )(unlift(Wallet.unapply))

  implicit val walletFormat: Format[Wallet] =
    Format(walletReads, walletWrites)
}

case class Seed(mnemonic: Seq[String], binaryKey: String)
object Seed {
  val seedReads: Reads[Seed] = (
  (JsPath \ "mnemonic").read[Seq[String]] and
  (JsPath \ "binaryKey").read[String]
  )(Seed.apply _)

  val seedWrites: Writes[Seed] = (
  (JsPath \ "mnemonic").write[Seq[String]] and
  (JsPath \ "binaryKey").write[String]
  )(unlift(Seed.unapply))

  implicit val seedFormat: Format[Seed] =
    Format(seedReads, seedWrites)
}

