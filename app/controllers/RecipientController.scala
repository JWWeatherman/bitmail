package controllers

import javax.inject.Inject

import model.WalletStorage
import play.api.mvc.Controller

class RecipientController @Inject()(
  val walletStorage: WalletStorage
) extends Controller  {

}
