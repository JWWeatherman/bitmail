package controllers

import javax.inject.Inject

import akka.actor.ActorRef
import forms.CreateWalletForm
import model.TransactionsHandler
import play.api.libs.json.{ JsValue, Json }
import play.api.mvc.{ Action, Controller, Request }
import play.modules.reactivemongo.ReactiveMongoApi
import bitcoin.WalletMaker
import com.google.inject.name.Named

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SenderController @Inject()(
                              val reactiveMongoApi: ReactiveMongoApi,
                              walletMaker: WalletMaker,
                              @Named("BitcoinClientActor") bitcoinClient : ActorRef
                       ) extends Controller with TransactionsHandler {
  def createWallet() = Action.async(parse.json) { implicit request : Request[JsValue] =>
    CreateWalletForm.form.bindFromRequest.fold(
      _ => Future.successful(BadRequest),
      data => {
        for {
          wallet <- insertWallet(walletMaker(data))
          response = wallet
        } yield {
          bitcoinClient ! wallet
          Ok(Json.toJson(response).toString)
        }
      }
    )
  }
}
