package model.models

case class BitcoinTransaction(publicAddressKey : String, transactionId : String, senderState: String, recipientState : String)

object BitcoinTransaction {
  final val publicAddressField = "publicAddress"
  final val transactionIdField = "transactionId"
  final val senderStateField = "senderState"
  final val recipientStateField = "recipientState"

  final val NotSent = "NotSent"
  final val Sent = "Sent"
  final val Bounced = "Bounced"
}