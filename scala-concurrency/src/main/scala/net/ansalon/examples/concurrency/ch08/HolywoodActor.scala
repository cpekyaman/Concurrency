package net.ansalon.examples.concurrency.ch08

import akka.actor.Actor

class HolywoodActor extends Actor {
	def receive = {
	  case role : String => println("Playing " + role + " from Thread " + Thread.currentThread().getName())
	  case other => println("Don't play with me: " + other)
	}
}