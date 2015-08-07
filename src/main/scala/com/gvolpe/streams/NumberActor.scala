package com.gvolpe.streams

import akka.actor.{Actor, ActorLogging, Props}

object NumberActor {
  def props = Props[NumberActor]
}

class NumberActor extends Actor with ActorLogging {

  def receive = {
    case n: Int =>
      log.info(s"N > $n")
  }

}
