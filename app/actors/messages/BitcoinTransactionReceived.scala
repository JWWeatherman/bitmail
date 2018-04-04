package actors.messages

import forms.{CreateWalletForm, Data}
import org.bitcoinj.core.Coin

case class BitcoinTransactionReceived(transData: Data,
                                      publicKeyAddress: String,
                                      transactionId: String,
                                      previousValue: Coin,
                                      newValue: Coin)
