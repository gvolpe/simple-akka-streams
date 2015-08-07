package com.gvolpe.streams

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.actor.ActorPublisher
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.forkjoin.ThreadLocalRandom

object StreamsApp extends App {

  implicit val system = ActorSystem("Sys")
  implicit val materializer = ActorMaterializer()

//  Source(1 to 3)
//    .map { i => println(s"A: $i"); i }
//    .map { i => println(s"B: $i"); i }
//    .map { i => println(s"C: $i"); i }
//    .runWith(Sink.onComplete (_ => system.shutdown() ))

  //ComplexGraph().run()
  //SimpleGraph().run()
  //CompositeGraph().run()

//  val source = Source.actorRef[Int](1000, OverflowStrategy.dropBuffer)
//  val ref = Flow[Int].to(Sink.ignore).runWith(source)

  val numberActor = system.actorOf(NumberActor.props)
  val pub = ActorPublisher[Int](numberActor)

  Source(pub).runWith(Sink.ignore)

  while(true) {
    val number = ThreadLocalRandom.current().nextInt(15680)
    numberActor ! number
  }

}
