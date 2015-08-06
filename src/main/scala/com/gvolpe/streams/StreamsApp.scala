package com.gvolpe.streams

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.gvolpe.streams.flows.CompositeGraph

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
  CompositeGraph().run()

}
