package com.gvolpe.streams

import akka.actor.{PoisonPill, ActorSystem}
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import com.gvolpe.streams.flows.CompositeGraph
import scala.concurrent.forkjoin.ThreadLocalRandom
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

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

//  val source = Source.actorRef[Int](1000, OverflowStrategy.dropBuffer)
//  val (actor, response) = source.via(Flow[Int]).toMat(Sink.foreach(println))(Keep.both).run()

  //val ref = Flow[Int].to(Sink.ignore).runWith(source)

//  val numberActor = system.actorOf(NumberActor.props)
//  val pub = ActorPublisher[Int](numberActor)
//
//  Source(pub).runWith(Sink.ignore)

//  def number = ThreadLocalRandom.current().nextInt(15680)
//
//  actor ! number
//  actor ! number
//  actor ! number
//  actor ! number
//
//  Thread.sleep(1000)
//
//  actor ! PoisonPill
//
//  response.onComplete {
//    case Success(_) =>
//      println("End...")
//      system.shutdown()
//    case Failure(e) =>
//      println(e.getMessage)
//  }
}
