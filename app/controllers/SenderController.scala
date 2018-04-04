package controllers

import javax.inject.Inject

import actors.ActorNames
import actors.messages.{ BitcoinTransactionReceived, EmailBounceCheck }
import akka.actor.ActorRef
import bitcoin.WalletMaker
import com.google.inject.name.Named
import forms.{ CreateWalletForm, Data }
import model.WalletStorage
import org.bitcoinj.core.Coin
import play.api.libs.json.{ JsString, JsValue, Json }
import play.api.mvc.{ Action, AnyContent, Controller, Request }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SenderController @Inject()(
                                  walletMaker : WalletMaker,
                              walletStorage : WalletStorage,
                              @Named("BitcoinClientActor") bitcoinClient : ActorRef,
                              @Named("NotificationSendingActor") notificationSendingActor : ActorRef,
                                @Named(ActorNames.EmailCommunications) emailCommunicationsActor : ActorRef

)
                        extends Controller {
  def createWallet() = Action.async(parse.json) { implicit request : Request[JsValue] =>
    CreateWalletForm.form.bindFromRequest.fold(
      _ => Future.successful(BadRequest),
      data => {
        val wallet = walletMaker(data)
        for {
          result <- walletStorage.insertWallet(wallet)
          response = wallet
        } yield {
          if (result.isDefined) {
            wallet.transData.senderEmail match {
              case Some("gifted.primate@protonmail.com") => // For front end developer to bypass blockchain
                notificationSendingActor ! BitcoinTransactionReceived(wallet.transData, wallet.publicKeyAddress, "faketransactionid", Coin.COIN, Coin.COIN)
              case _ =>
                bitcoinClient ! wallet
            }
            Ok(Json.toJson(response).toString)
          }
          else
            InternalServerError("Cannot save wallet")
        }
      }
    )
  }

  def readyWallet() = Action { implicit request : Request[AnyContent] =>
    val w = Data("Chtg25KIUU2nIRyvVMzmbQ@protonmail.com", Some("console.rastling@protonmail.com"), "Here's your money!", remainAnonymous = false)
    /*    for {
      wallet <- insertWallet(walletMaker(w))
    } yield {
      bitcoinClient ! wallet
      Ok(Json.prettyPrint(Json.toJson(wallet)))
    }*/
    val wallet = walletMaker(w)
    bitcoinClient ! wallet
    Ok(Json.prettyPrint(Json.toJson(wallet)))
  }

  def checkBounces() = Action { implicit request : Request[AnyContent] =>
    emailCommunicationsActor ! EmailBounceCheck(0)
    Ok(Json.prettyPrint(JsString("bounceCheck!")))
  }
}
