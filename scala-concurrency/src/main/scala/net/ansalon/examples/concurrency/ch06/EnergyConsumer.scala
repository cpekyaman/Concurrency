package net.ansalon.examples.concurrency.ch06

import scala.actors.Actor._

object EnergyConsumer {
  val source = EnergySource.create

  def main(args: Array[String]): Unit = {
    println("Energy level at start: " + source.unitsAvailable)

    val caller = self
    for (i <- 1 to 10) actor {
      for (j <- 1 to 7) source.consume(1)
      caller ! true
    }

    for (i <- 1 to 10) { receiveWithin(1000) { case message => } }
    
    println("Energy level at end: " + source.unitsAvailable)
    println("Usage: " + source.usages)
    
    source.stop
  }
}