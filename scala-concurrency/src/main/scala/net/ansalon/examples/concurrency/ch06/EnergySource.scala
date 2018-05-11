package net.ansalon.examples.concurrency.ch06

import akka.stm._

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class EnergySource private () {
  private val MAXLEVEL = 100L
  val level = Ref(MAXLEVEL)
  val usageCount = Ref(0L)
  val keepRunning = Ref(true)

  private def init {
    EnergySource.replenishTimer.schedule(new Runnable() {
      def run {
        replenish
        if (keepRunning.get)
          EnergySource.replenishTimer.schedule(this, 1, TimeUnit.SECONDS)
      }
    }, 1, TimeUnit.SECONDS)
  }

  def stop {
    keepRunning.swap(false)
    EnergySource.replenishTimer.shutdown()
  }

  def replenish {
    atomic {
      if (level.get < MAXLEVEL) level.swap(level.get + 1)
    }
  }

  def consume(units: Long): Boolean = {
    atomic {
      if (units > 0 && level.get >= units) {
        level.swap(level.get - units)
        usageCount.swap(usageCount.get() + 1)
        true
      } else false
    }
  }

  def unitsAvailable = level.get()
  def usages = usageCount.get()
}

object EnergySource {
  val replenishTimer = Executors.newScheduledThreadPool(10)

  def create() = {
    val energySource = new EnergySource
    energySource.init
    energySource
  }
}