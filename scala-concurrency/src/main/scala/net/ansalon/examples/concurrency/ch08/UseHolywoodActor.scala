package net.ansalon.examples.concurrency.ch08

import akka.actor.Actor

object UseHolywoodActor {
	def main(args: Array[String]) {
		val johnny = Actor.actorOf[HolywoodActor].start()
		
		johnny ! "Jack Sparrow"
		Thread.sleep(100)
		johnny ! "Willy Wonka"
		Thread.sleep(100)
		johnny ! "Jack Sparrow"
		johnny ! "Willy Wonka"
		johnny ! "Jack Sparrow"
		Thread.sleep(100)
		johnny ! "Willy Wonka"
		johnny ! 35
		Thread.sleep(100)
		johnny ! "Willy Wonka"
		johnny ! false
		Thread.sleep(100)
		johnny ! "Willy Wonka"
		
		Thread.sleep(1000)
		johnny.stop()
	}
}