package actors.messages

import forms.CreateWalletForm
import org.bitcoinj.core.Coin

case class BitcoinTransactionReceived(transData : CreateWalletForm.Data, previousValue : Coin, newValue : Coin)
