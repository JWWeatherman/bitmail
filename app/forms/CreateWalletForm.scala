package forms

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.functional.syntax._
import play.api.libs.json.{ Format, JsPath, Reads, Writes }

/**
  * The form which handles the submission of the credentials.
  */
object CreateWalletForm {

  /**
    * A play framework form.
    *
    * { "recipientEmail": "duncan.nevin@gmail.com",
    *   "senderEmail": "duncan.nevin@gmail.com",
    *   "senderMessage": "dfsaf",
    *   "remainAnonymous": false
    *  }
    */
  val form = Form(
    mapping(
      "recipientEmail" -> email,
      "senderEmail" -> optional(email),
      "senderMessage" -> text,
      "remainAnonymous" -> boolean
    )(Data.apply)(Data.unapply)
  )

  /**
    * The form data.
    *
    * @param recipientEmail The email of recipient
    * @param senderEmail The password of the sender, can be left null if anonymous.
    * @param remainAnonymous Indicates if sender wants to remain anonymous.
    */
  case class Data(
                 recipientEmail: String,
                 senderEmail: Option[String],
                 senderMessage: String,
                 remainAnonymous: Boolean
                 )
  object Data {
    val dataReads: Reads[Data] = (
    (JsPath \ "recipientEmail").read[String] and
    (JsPath \ "senderEmail").readNullable[String] and
    (JsPath \ "senderMessage").read[String] and
    (JsPath \ "remainAnonymous").read[Boolean]
    )(Data.apply _)

    val dataWrites: Writes[Data] = (
    (JsPath \ "recipientEmail").write[String] and
    (JsPath \ "senderEmail").writeNullable[String] and
    (JsPath \ "senderMessage").write[String] and
    (JsPath \ "remainAnonymous").write[Boolean]
    )(unlift(Data.unapply))

    implicit val dataFormat: Format[Data] =
      Format(dataReads, dataWrites)
  }
}
