package net.ansalon.examples.concurrency.ch05.queue;

import java.io.File;
import java.util.concurrent.BlockingQueue;

class Indexer implements Runnable {
	private final BlockingQueue<File> queue;
	
	public Indexer(final BlockingQueue<File> queue) {
		this.queue = queue;
	}
	
	@Override
	public void run() {
		try {
			while(true)
				index(queue.take());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	private void index(File file)
	throws InterruptedException {
		
	}
}
