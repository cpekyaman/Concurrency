package net.ansalon.examples.concurrency.ch08.primes

import akka.dispatch.Future
import akka.actor.{Actor, Actors}

object PrimeRunner {
  def countPrimes(number: Int, partitions: Int) = {
    val chunks = number / partitions
    val results = new Array[Future[Integer]](partitions)
    var index = 0
    while (index < partitions) {
      val lower = index * chunks + 1
      val upper = if (index == partitions - 1) number else lower + chunks - 1
      val bounds = new PrimeRange(lower, upper)
      val primeFinder = Actor.actorOf[PrimeActor].start()
      results(index) = (primeFinder !!! bounds).asInstanceOf[Future[Integer]]
      index += 1
    }

    var count = 0
    index = 0
    while (index < partitions) {
      count += results(index).await.result.get.intValue()
      index += 1
    }
    Actors.registry.shutdownAll
    count
  }

  def main(args: Array[String]) {
	val start = System.nanoTime();
	val numberOfPrimes = countPrimes(1000000, 100);
	val end = System.nanoTime();
		
	System.out.println("Number of primes under 1000000 is " + numberOfPrimes);
	System.out.println("Time (seconds) taken is " + (end - start) / 1.0e9);
  }
}