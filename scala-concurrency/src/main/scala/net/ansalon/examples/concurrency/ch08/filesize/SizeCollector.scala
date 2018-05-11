package net.ansalon.examples.concurrency.ch08.filesize

import akka.actor.{ Actor, ActorRef, Actors }

class SizeCollector extends Actor {
  private var totalSize = 0L
  private var numberOfFilesToProcess = 0L
  private var activeProcessors = 0L
  private var filesToProcess = List.empty[String]
  private var processors = List.empty[ActorRef]
  private val start = System.nanoTime()

  def receive = {
    case RequestFile =>
      println("Collector got RequestFile")
      
      processors = self.getSender().get :: processors
      sendFileToProcess
    case ProcessFile(fileName) =>
      println("Collector got ProcessFile")
      
      filesToProcess = fileName :: filesToProcess
      numberOfFilesToProcess += 1
      sendFileToProcess      
    case FileSize(size) =>
      println("Collector got FileSize")
      
      totalSize += size
      numberOfFilesToProcess -= 1
      activeProcessors -= 1
      if (numberOfFilesToProcess == 0 && activeProcessors == 0) {
        val end = System.nanoTime()
        println("Total size is " + totalSize)
        println("Time taken is " + (end - start) / 1.0e9)
        println("Shutdown...")
        Actors.registry.shutdownAll
      }
  }

  def sendFileToProcess {
    if (!filesToProcess.isEmpty && !processors.isEmpty) {
      if(processors.head.isRunning) {
        activeProcessors += 1
        processors.head ! ProcessFile(filesToProcess.head)
        processors = processors.tail
      	filesToProcess = filesToProcess.tail
      } else {
        println("Head Of List is not running !!!")
      }      
    }
  }
}