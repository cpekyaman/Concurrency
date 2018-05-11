package net.ansalon.examples.concurrency.ch09

import groovyx.gpars.dataflow.*
import groovyx.gpars.group.*

class FileSizeFinder {
	private final pendingFiles = new DataflowQueue()
	private final sizes = new DataflowQueue()
	private final group = new DefaultPGroup()

	def findSize(File file) {
		def size = 0
		
		if(!file.isDirectory())
			size = file.length()
		else {
			def children = file.listFiles()
			if (children != null) {
				children.each { child ->
					if(child.isFile())
						size += child.length()
					else {
						pendingFiles << 1
						group.task { findSize(child) }
					}
				}
			}
		}
		
		pendingFiles << -1
		sizes << size
	}
	
	def findTotalFileSize(File file) {
		pendingFiles << 1
		group.task { findSize(file) }
		
		int filesToVisit = 0
		long totalSize = 0
		
		while(true){
			totalSize += sizes.val
			if(!(filesToVisit += (pendingFiles.val + pendingFiles.val))) break
		}
		totalSize
	}
}
