package forms

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.functional.syntax._
import play.api.libs.json.{ Format, JsPath, Reads, Writes }

/**
  * The form which handles the submission of the credentials.
  */


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

    val recipientEmailField = "recipientEmail"
    val senderEmailField = "senderEmail"
    val senderMessageField = "senderMessage"
    val remaindAnonymousField = "remainAnonymous"

    val dataReads : Reads[Data] = (
      (JsPath \ recipientEmailField).read[String] and
        (JsPath \ senderEmailField).readNullable[String] and
        (JsPath \ senderMessageField).read[String] and
        (JsPath \ remaindAnonymousField).read[Boolean]
      ) (Data.apply _)

    val dataWrites : Writes[Data] = (
      (JsPath \ recipientEmailField).write[String] and
        (JsPath \ senderEmailField).writeNullable[String] and
        (JsPath \ senderMessageField).write[String] and
        (JsPath \ remaindAnonymousField).write[Boolean]
      ) (unlift(Data.unapply))

    implicit val dataFormat : Format[Data] =
      Format(dataReads, dataWrites)

  }

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

}
