package email

import model.models.SnailWallet

import scalatags.Text.all._

case class BounceNotificationEmailMessage(walletSenderEmail: String, wallets: Seq[SnailWallet])
    extends EmailMessage {
  override def asHtml: String = {
    html(
      body(
        p(s"Hello $walletSenderEmail"),
        p(s"Your coin transmission${if (wallets.length > 1) "s" else "" } to ${wallets.head.transData.recipientEmail} could not be delivered because the email address did not exist."),
        p("Please check the email address and try again"),
        p("Thanks,"),
        p("The Management!")
      )
    ).render
  }

  override def from: String = "notifications@bitcoinsnail.com"

  override def to: String = walletSenderEmail
}
