package net.ansalon.examples.concurrency.ch04.coordination;

import java.io.File;

public abstract class AbstractDirSizeFinder {
	public void run(final String rootDir) 
	throws Exception {
		final long start = System.nanoTime();
		final long total = getTotalSize(new File(rootDir));
		final long end = System.nanoTime();
		
		System.out.println("==============================");
		System.out.println("Search Root: " + rootDir);
		System.out.println("Total Size: " + total);
		System.out.println("Time taken: " + (end - start) / 1.0e9);
		System.out.println("==============================");
	}
	
	protected abstract long getTotalSize(final File file) throws Exception;
}
