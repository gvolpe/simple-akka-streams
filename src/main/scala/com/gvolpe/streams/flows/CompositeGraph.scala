package com.gvolpe.streams.flows

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.FlowGraph.Implicits._
import akka.stream.scaladsl._

object CompositeGraph {

  val payloadFilterFlow = FlowGraph.partial() { implicit builder =>
    val b = builder.add(Broadcast[String](2))
    val filter = builder.add(Flow[String] filter (Seq("a","e","i","o","u").contains(_)))
    val filterNot = builder.add(Flow[String] filter (!Seq("a","e", "i", "o", "u").contains(_)))

    b ~> filter
    b ~> filterNot

    UniformFanOutShape(b.in, filter.outlet, filterNot.outlet)
  }.named("payloadFilterFlow")

  val eventInboundFlow = FlowGraph.partial() { implicit builder =>
    val headerEnricherFlow = builder.add(Flow[String] map (_.toLowerCase))
    val payloadFilter = builder.add(payloadFilterFlow)

    headerEnricherFlow.outlet ~> payloadFilter.in

    UniformFanOutShape(headerEnricherFlow.inlet, payloadFilter.out(0), payloadFilter.out(1))
  }.named("eventInboundFlow")

  def apply()(implicit system: ActorSystem): RunnableGraph[Unit] = FlowGraph.closed() { implicit builder =>
    val source: Source[String, Unit] = Source(('A' to 'Z').map(_.toString))

    val eventInbound = builder.add(eventInboundFlow)
    source ~> eventInbound

    eventInbound.out(0) ~> Sink.ignore
    eventInbound.out(1) ~> Sink.foreach(println)
  }

}
