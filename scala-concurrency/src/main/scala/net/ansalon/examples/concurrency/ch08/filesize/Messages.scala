package net.ansalon.examples.concurrency.ch08.filesize

case object RequestFile
case class ProcessFile(val fileName: String)
case class FileSize(val size: Long)