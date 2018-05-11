package net.ansalon.examples.concurrency.ch08.murmur

trait EnergySource {
  def getUnitsAvailable(): Long
  def getUsageCount(): Long
  def useEnergy(units: Long): Unit
}