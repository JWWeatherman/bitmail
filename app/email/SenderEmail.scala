package email

import fr.acinq.bitcoin.Crypto.PublicKey
import play.api.libs.mailer._

class SenderEmail(mailerClient: MailerClient) extends Email {
  def send(templateName: String, recipientEmail: String, senderEmail: Option[String], publicKeyAddress: String, message: String, amount: String) = {
    senderEmail match {
      case Some(email) =>
        val template = templateName match {
          case "fundsReceivedSender" => fundsReceivedSender(recipientEmail, email, publicKeyAddress, message, amount)
          case _ => throw new Exception("NOT A VALID TEMPLATE NAME")
        }
        sendMailBatch(mailerClient, Seq(email), recipientEmail, template)
      case None => None
    }
  }

  def fundsReceivedSender(recipientEmail: String, senderEmail: String, publicKeyAddress: String, message: String, amount: String): String = {
    s"""
       |<html>
       |  <body>
       |    <p>Hello $senderEmail,</p>
       |    <p>Your gift to $recipientEmail for $amount credit to address $publicKeyAddress.</p>
       |    <br/>
       |    <p>To monitor this transaction go to this link.</p>
       |    <a href="http://localhost:8080/#/status/$publicKeyAddress">Check Status!</a>
       |  </body>
       |</html>
    """.stripMargin
  }
}
