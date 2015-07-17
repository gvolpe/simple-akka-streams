package com.gvolpe.streams.flows

import akka.actor.ActorSystem
import akka.stream.scaladsl.FlowGraph.Implicits._
import akka.stream.scaladsl._

object SimpleGraph {

  def apply()(implicit system: ActorSystem): RunnableGraph[Unit] = FlowGraph.closed() { implicit builder =>
    val in = Source(1 to 10)
    val out = Sink.onComplete(_ => system.shutdown())

    val broadcast = builder.add(Broadcast[Int](2))
    val merge = builder.add(Merge[Int](2))

    val f1, f2, f3, f4 = Flow[Int].map(_ + 10)
    val console = Flow[Int].map(println)

    in ~> f1 ~> broadcast ~> f2 ~> merge ~> f3 ~> console ~> out
                broadcast ~> f4 ~> merge
  }

}
