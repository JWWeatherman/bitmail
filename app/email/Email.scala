package email

import play.api.libs.mailer._

trait Email {
  def sendMailBatch(mailerClient: MailerClient, too: Seq[String], from: String, template: String): Unit = {
    val email = Email(
      "Bitmail",
      "doohickeymastermind@gmail.com", // <--- needs to be email associated with mail account in app config
      too,
      bodyHtml = Some(template)
    )
    mailerClient.send(email)
  }
}
