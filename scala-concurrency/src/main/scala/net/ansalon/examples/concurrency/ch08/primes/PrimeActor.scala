package net.ansalon.examples.concurrency.ch08.primes

import akka.actor.Actor

class PrimeActor extends Actor {
	def receive = {
	  case bounds: PrimeRange => self.reply_?(countPrimes(bounds))
	  case _ => println("require a range")
	}
	
	private def countPrimes(bounds: PrimeRange) = {
	  PrimeFinder.countInRange(bounds)
	}
}