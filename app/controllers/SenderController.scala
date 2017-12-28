package controllers

import javax.inject.Inject

import forms.CreateWalletForm
import model.TransactionsHandler
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, Controller, Request}
import play.modules.reactivemongo.ReactiveMongoApi
import bitcoin.WalletMaker
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class SenderController @Inject()(
                              val reactiveMongoApi: ReactiveMongoApi,
                              wallet: WalletMaker
                              ) extends Controller with TransactionsHandler {

  def createWallet() = Action.async(parse.json)  { implicit request: Request[JsValue] =>
    CreateWalletForm.form.bindFromRequest.fold(
      _ => Future.successful(BadRequest),
      data => {
        for {
          insertWallet <- insertWallet(wallet.genWallet(data))
          response = Json.obj("publicKey" -> insertWallet.publicKey, "transData" -> insertWallet.transData)
        } yield Ok(response.toString)
      }
    )
  }
}
