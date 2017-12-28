package controllers

import javax.inject.Inject

import forms.CreateWalletForm
import model.Trivia
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, Controller, Request}
import play.modules.reactivemongo.ReactiveMongoApi
import bitcoin.WalletMaker

class SendController @Inject()(
                              val reactiveMongoApi: ReactiveMongoApi,
                              wallet: WalletMaker
                              ) extends Controller with Trivia {

  def createWallet() = Action(parse.json)  { implicit request: Request[JsValue] =>
    CreateWalletForm.form.bindFromRequest.fold(
      formWithError => BadRequest,
      data => Ok(Json.toJson(wallet.genWallet))
    )
  }
}
