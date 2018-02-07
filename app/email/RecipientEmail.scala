package email

import play.api.libs.mailer._

class RecipientEmail(mailerClient: MailerClient) extends Email {

  def send(templateName: String, recipientEmail: String, senderEmail: Option[String], publicKeyAddress: String, message: String, amount: String) = {
    val sEmail = senderEmail match {
      case Some(email) => email
      case None => "Some nice fellow"
    }
    val template = templateName match {
      case "fundsReceiveRecipient" => fundsReceiveRecipient(recipientEmail, sEmail, publicKeyAddress, message, amount)
      case _ => throw new Exception("NOT A VALID TEMPLATE NAME")
    }
    sendMailBatch(mailerClient, Seq(recipientEmail), sEmail, template)
  }

  def fundsReceiveRecipient(recipientEmail: String, senderEmail: String, publicKeyAddress: String, message: String, amount: String): String = {
    s"""
       |<html>
       |  <body>
       |    <p>Hello $recipientEmail,</p>
       |    <p>$senderEmail has sent you $amount!</p>
       |    <br/>
       |    <p>$message</p>
       |    <br/>
       |    <p>Please go to the following link to learn how to claim this bitcoin.</p>
       |    <a href="http://localhost:8080/#/recipient/$publicKeyAddress">Claim your coin!</a>
       |  </body>
       |</html>
    """.stripMargin
  }
}
