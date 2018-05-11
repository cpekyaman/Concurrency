package net.ansalon.examples.concurrency.ch05.queue;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DesktopSearch {
	private static final int BOUND = 2000;
	private static final int N_CONSUMERS = 20;
	
	public static void main(String[] args) {
		BlockingQueue<File> queue = new LinkedBlockingQueue<File>( BOUND );
		FileFilter filter = new FileFilter () {
			public boolean accept (File file ) { return true; }
		};
		for (File root : roots)
			new Thread (new Crawler(queue, root, filter)).start();
		
		for (int i = 0; i < N_CONSUMERS ; i++)
			new Thread (new Indexer(queue)).start();
	}
}
