package controllers

import javax.inject.Inject

import actors.NotificationSendingActor
import actors.messages.BitcoinTransactionReceived
import akka.actor.ActorRef
import forms.CreateWalletForm
import model.TransactionsHandler
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent, Controller, Request}
import play.modules.reactivemongo.ReactiveMongoApi
import bitcoin.WalletMaker
import com.google.inject.name.Named
import org.bitcoinj.core.Coin

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SenderController @Inject()(
                              val reactiveMongoApi: ReactiveMongoApi,
                              walletMaker: WalletMaker,
                              @Named("BitcoinClientActor") bitcoinClient : ActorRef,
                              @Named("NotificationSendingActor") notificationSendingActor: ActorRef
                       ) extends Controller with TransactionsHandler {
  def createWallet() = Action.async(parse.json) { implicit request : Request[JsValue] =>
    CreateWalletForm.form.bindFromRequest.fold(
      _ => Future.successful(BadRequest),
      data => {
        for {
          wallet <- insertWallet(walletMaker(data))
          response = wallet
        } yield {
          wallet.transData.senderEmail match {
            case Some("gifted.primate@protonmail.com") => // For front end developer to bypass blockchain
              notificationSendingActor ! BitcoinTransactionReceived(wallet.transData, wallet.publicKeyAddress, Coin.COIN, Coin.COIN)
            case _ =>
              bitcoinClient ! wallet
          }
          Ok(Json.toJson(response).toString)
        }
      }
    )
  }

  def readyWallet() = Action.async { implicit request : Request[AnyContent] =>
    val w = CreateWalletForm.Data("gifted.primate@protonmail.com", Some("doohickeymastermind@protonmail.com"), "Here's your money!", remainAnonymous = false)
    for {
      wallet <- insertWallet(walletMaker(w))
    } yield {
      bitcoinClient ! wallet
      Ok(Json.prettyPrint(Json.toJson(wallet)))
    }
  }
}
