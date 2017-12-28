package bitcoin

import play.mvc.Http
import fr.acinq.bitcoin._
import fr.acinq.bitcoin.Crypto._
import MnemonicCode._
import forms.CreateWalletForm
import model.models.{Seed, Wallet}
import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath, Reads, Writes}

class WalletMaker extends Bitcoin {

  def genMnemonic(hexString: String): List[String] = toMnemonics(fromHexString(hexString))

  def genSeed(mnemonics: Seq[String]): BinaryData = toSeed(mnemonics, "TREZOR")

  def genWallet(transData: CreateWalletForm.Data) = {
    val hex: String = hexStringGen
    val mnemonic: List[String] = genMnemonic(hex)
    val binaryKey: BinaryData = genSeed(mnemonic)
    val privKey = privateKeyGen(binaryKey)
    val pubKey = publicKeyUncompressed(privKey)

    Wallet(transData, Seed(mnemonic, binaryKey.toString), privKey.toString, pubKey.toString)
  }
}
