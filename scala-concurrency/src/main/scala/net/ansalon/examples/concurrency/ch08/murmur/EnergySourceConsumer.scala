package net.ansalon.examples.concurrency.ch08.murmur

import akka.actor.TypedActor

object EnergySourceConsumer {
  def main(args: Array[String]): Unit = {
    println("Thread in main: " + Thread.currentThread().getName())
    
    val energySource = TypedActor.newInstance(classOf[EnergySource], classOf[EnergySourceImpl])
    println("Energy units " + energySource.getUnitsAvailable)
    println("Firing two requests for use energy")
    
    energySource.useEnergy(10)
    println("Fired two requests for use energy")
    Thread.sleep(100)
    
    println("Firing one more requests for use energy")
    energySource.useEnergy(10)
    Thread.sleep(1000);
    
    println("Energy units " + energySource.getUnitsAvailable)
    println("Usage " + energySource.getUsageCount)
    TypedActor.stop(energySource)
  }
}