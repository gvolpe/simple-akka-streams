package com.gvolpe.streams.flows

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream._
import akka.stream.scaladsl.FlowGraph.Implicits._
import akka.stream.scaladsl._

object CompositeGraph {

  val logginLevel = Attributes.logLevels(onElement = Logging.InfoLevel)

  val payloadFilterFlow = FlowGraph.partial() { implicit builder =>

    val b = builder.add(Broadcast[String](2))

    //    val filterFlow = ((Flow[String]).log("BEFORE Filter vocals") filter (Seq("a","e","i","o","u").contains(_)))
    //      .log("AFTER Filter vocals.").withAttributes(logginLevel)
    val filterFlow = (Flow[String] filter (Seq("a", "e", "i", "o", "u").contains(_)))
      .log("AFTER Filter vocals.").withAttributes(logginLevel)
    val filter = builder.add(filterFlow)

    val filterNot = builder.add(Flow[String] filter (!Seq("a", "e", "i", "o", "u").contains(_)))
    b ~> filter
    b ~> filterNot

    UniformFanOutShape(b.in, filter.outlet, filterNot.outlet)
  }.named("payloadFilterFlow")

  val eventInboundFlow = FlowGraph.partial() { implicit builder =>
    val lowerCaseFlow = (Flow[String] map (_.toLowerCase))
      .log("AFTER Lower case map.").withAttributes(logginLevel)
    val headerEnricherFlow = builder.add(lowerCaseFlow)
    val payloadFilter = builder.add(payloadFilterFlow)

    headerEnricherFlow.outlet ~> payloadFilter.in

    UniformFanOutShape(headerEnricherFlow.inlet, payloadFilter.out(0), payloadFilter.out(1))
  }.named("eventInboundFlow")

  def apply()(implicit system: ActorSystem): RunnableGraph[Unit] = FlowGraph.closed() { implicit builder =>
    val source = Source(('A' to 'Z').map(_.toString))
                                    .log("SOURCE ")
                                    .withAttributes(logginLevel)

    val printFlow = builder.add(Flow[String] map { e => println(e); e })
    val ignoreFlow = builder.add(Flow[String])

    val eventInbound = builder.add(eventInboundFlow)
    source ~> eventInbound

    val merge = builder.add(Merge[String](2))

    eventInbound.out(0) ~> printFlow ~> merge
    eventInbound.out(1) ~> ignoreFlow ~> merge
    merge ~> Sink.onComplete { _ => system.shutdown() }
  }

}
