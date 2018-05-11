package net.ansalon.examples.concurrency.ch08.filesize

import akka.actor.{ Actor, ActorRef }

class FileProcessor(val collector: ActorRef) extends Actor {
  override def preStart = requestFile
  def requestFile = { collector ! RequestFile }

  def receive = {
    case ProcessFile(fileName) => processFile(fileName)
  }

  def processFile(fileName: String) {
    val file = new java.io.File(fileName)

    var size = 0L
    if (file.isFile()) {
      size = file.length()
    } else {
      val childs = file.listFiles()
      if (childs != null) {
        for (child <- childs) {
          if (child.isFile()) {
            size += child.length()
          } else {
            collector ! ProcessFile(child.getPath())
          }
        }
      }
    }

    if(collector.isRunning) {
      collector ! FileSize(size)
      requestFile
    } else {
      println("Collector is not running!!!")
      self.stop()
    }
  }
}