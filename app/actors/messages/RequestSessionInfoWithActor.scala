package actors.messages

import akka.actor.ActorRef

case class RequestSessionInfoWithActor(actor : ActorRef)
