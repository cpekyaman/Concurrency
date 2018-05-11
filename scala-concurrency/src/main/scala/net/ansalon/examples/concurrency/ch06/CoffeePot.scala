package net.ansalon.examples.concurrency.ch06

import akka.stm._
import akka.util.duration.intToDurationInt
import java.util.{ Timer, TimerTask }

object CoffeePot {
  private val cups = Ref(24)
  private val start = System.nanoTime()
  private val factory = TransactionFactory(blockingAllowed = true, timeout = 6 seconds)
  
  private def checkpoint = (System.nanoTime() - start) / 1.0e9

  def fill(requested: Int) {
    atomic(factory) {
      if (cups.get < requested) {
        println("retry at ... " + checkpoint)
        retry
      }
      cups.swap(cups.get - requested)
      println("Filled " + requested + " cups at " + checkpoint)
    }
  }

  def main(args: Array[String]): Unit = {
    val timer = new Timer(true)
    timer.schedule(new TimerTask() {
      def run() {
        println("Refilling.... at " + checkpoint)
        cups.swap(24)
      }
    }, 5000)
    
    fill(20)
    fill(10)
    try {
      fill(22)
    } catch {
      case ex => println("Failed: " + ex.getMessage())
    }
  }

}