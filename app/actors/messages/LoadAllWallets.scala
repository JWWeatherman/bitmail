package actors.messages

import model.models.SnailTransaction

case class LoadAllWallets(wallets : List[SnailTransaction])

