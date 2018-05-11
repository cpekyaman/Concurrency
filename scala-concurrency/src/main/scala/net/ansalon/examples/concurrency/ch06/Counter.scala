package net.ansalon.examples.concurrency.ch06

import akka.stm._

class Counter {
	val count = Ref(1)
	
	def decrement {
	  atomic {
	    deferred { println("Transaction SUCCESS !!!") }
	    compensating { println("Transaction FAILED !!!") }
	    
	    if(count.get <= 0) throw new RuntimeException("Count is consumed")
	    
	    count.swap(count.get - 1)
	  }
	}
}

object Counter {
  def main(args: Array[String]) {
   val counter = new Counter
   println("First Try")
   counter.decrement
   
   println("Second Try")
   try {
     counter.decrement
   } catch {
     case ex => println(ex.getMessage())
   }
  }
}