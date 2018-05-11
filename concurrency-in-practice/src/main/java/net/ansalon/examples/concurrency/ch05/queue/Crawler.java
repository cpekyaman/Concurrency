package net.ansalon.examples.concurrency.ch05.queue;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;

class Crawler implements Runnable {
	private final BlockingQueue<File> queue;
	private final FileFilter fileFilter;
	private final File root;
	
	Crawler(final BlockingQueue<File> queue, File root, FileFilter fileFilter) {
		this.queue = queue;
		this.root = root;
		this.fileFilter = fileFilter;
	}
	
	@Override
	public void run() {
		try {
			crawl(root);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	private void crawl(File root) 
	throws InterruptedException {
		File[] files = root.listFiles(fileFilter);
		if(files != null) {
			for(File file : files) {
				if(file.isDirectory()) crawl(file);
				else if(! indexed(file)) queue.put(file);
			}
		}
	}
}
