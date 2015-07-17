package com.gvolpe.streams.flows

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import akka.stream.scaladsl.FlowGraph.Implicits._

object ComplexGraph {

  def apply()(implicit system: ActorSystem): RunnableGraph[Unit] = FlowGraph.closed() { implicit builder =>
    val A: Outlet[Int] = builder.add(Source(1 to 100))
    val B: UniformFanOutShape[Int, Int] = builder.add(Broadcast[Int](2))
    val C: UniformFanInShape[Int, Int] = builder.add(Merge[Int](2))
    val D: FlowShape[Int, Int] = builder.add(Flow[Int].map(_ + 1))
    val E: UniformFanOutShape[Int, Int] = builder.add(Balance[Int](2))
    val F: UniformFanInShape[Int, Int] = builder.add(Merge[Int](2))
    val G: Inlet[Any] = builder.add(Sink.foreach(println))

              C <~ F
    A ~> B ~> C ~> F
         B ~> D ~> E ~> F
                   E ~> G
  }

}
