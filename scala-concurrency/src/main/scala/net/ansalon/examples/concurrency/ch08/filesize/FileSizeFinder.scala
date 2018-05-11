package net.ansalon.examples.concurrency.ch08.filesize

import akka.actor.Actor

object FileSizeFinder {
  def findSize(fileName: String) {
    val sizeCollector = Actor.actorOf[SizeCollector].start()
    sizeCollector ! ProcessFile(fileName)
    for (i <- 1 to 80)
      Actor.actorOf(new FileProcessor(sizeCollector)).start()
  }
  
  def main(args: Array[String]): Unit = {
    findSize("E:\\Setup")
    findSize("E:\\Workspace")
  }
}