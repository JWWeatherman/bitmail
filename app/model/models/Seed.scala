package model.models

import play.api.libs.functional.syntax._
import play.api.libs.json._

/*
* seed data
* @property mnemonic {Seq[String]} 12 backup word
* @property binaryKey {String} binary ket created from mnemonic
* */

case class Seed(mnemonic: Seq[String], binaryKey: String)
object Seed {
  implicit val jsonFormat = Json.format[Seed]

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

