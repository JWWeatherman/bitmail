package bitcoin

import fr.acinq.bitcoin.{Base58, Base58Check, BinaryData}
import fr.acinq.bitcoin.Crypto.{PrivateKey, PublicKey}
import scala.util._
import java.security._

class Bitcoin {
  def random = new Random(new SecureRandom())

  def hexStringGen = random.nextLong.toHexString + random.nextLong.toHexString

  def binaryGen(hex: String): BinaryData = BinaryData(hex)

  def privateKeyGen(key: BinaryData): PrivateKey = PrivateKey(key, compressed = false)

  def compressPrivateKey(key: BinaryData) = PrivateKey(key, compressed = true)

  def publicKeyUncompressed(priv: PrivateKey): PublicKey = priv.publicKey

  def pubkeyAddress(pubKey: PublicKey): String = Base58Check.encode(Base58.Prefix.PubkeyAddress, pubKey.hash160)

  def secretKey(priv: PrivateKey) = Base58Check.encode(Base58.Prefix.SecretKey, priv.toBin)

  def publicKeyCompressed(priv: PrivateKey): PublicKey = priv.publicKey.copy(compressed = true)
}
