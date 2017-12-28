package controllers

import javax.inject.Inject

import model.TransactionsHandler
import play.api.mvc.Controller
import play.modules.reactivemongo.ReactiveMongoApi

class RecipientController @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller with TransactionsHandler {

}
