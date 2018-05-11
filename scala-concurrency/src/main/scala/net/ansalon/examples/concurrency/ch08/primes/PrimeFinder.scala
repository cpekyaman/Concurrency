package net.ansalon.examples.concurrency.ch08.primes

import java.math

object PrimeFinder {
	def isPrime(number: Int) = {
	  if(number <= 1) false
	  for (i <- 2 until scala.math.sqrt(number).toInt)
	    if (number % 2 == 0) false
	  true
	}
	
	def countInRange(bounds: PrimeRange) = {
	  var total = 0
	  for(i <- bounds.lower until bounds.upper)
	    if(isPrime(i)) total += 1
	  total
	}
}