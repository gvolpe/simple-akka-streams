package com.gvolpe.streams

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

object SimpleFlowGraph extends App {

  implicit val system = ActorSystem("Sys")
  implicit val materializer = ActorMaterializer()

//  Source(1 to 3)
//    .map { i => println(s"A: $i"); i }
//    .map { i => println(s"B: $i"); i }
//    .map { i => println(s"C: $i"); i }
//    .runWith(Sink.onComplete (_ => system.shutdown() ))

  val graph = FlowGraph.closed() { implicit builder: FlowGraph.Builder[Unit] =>
    import FlowGraph.Implicits._

    val in = Source(1 to 10)
    val out = Sink.onComplete (_ => system.shutdown() )

    val broadcast = builder.add(Broadcast[Int](2))
    val merge = builder.add(Merge[Int](2))

    val f1, f2, f3, f4 = Flow[Int].map(_ + 10)
    val console = Flow[Int].map(println)

    in ~> f1 ~> broadcast ~> f2 ~> merge ~> f3 ~> console ~> out
                broadcast ~> f4 ~> merge
  }

  graph.run()

}
