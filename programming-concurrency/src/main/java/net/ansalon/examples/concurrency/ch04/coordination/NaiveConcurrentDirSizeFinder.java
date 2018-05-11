package net.ansalon.examples.concurrency.ch04.coordination;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NaiveConcurrentDirSizeFinder extends AbstractDirSizeFinder {
	private long getTotalSize(final ExecutorService service, final File file) 
	throws InterruptedException, ExecutionException, TimeoutException {
		if (file.isFile())
			return file.length();
		
		long total = 0;
		final File[] children = file.listFiles();
		if (children != null) {
			final List<Future<Long>> partialTotalFutures = new ArrayList<Future<Long>>();
			
			for (final File child : children) {
				partialTotalFutures.add(service.submit(new Callable<Long>() {
					public Long call() 
					throws InterruptedException, ExecutionException, TimeoutException {
						return getTotalSize(service, child);
					}
				}));
			}
			
			for (final Future<Long> partialTotalFuture : partialTotalFutures)
				total += partialTotalFuture.get(100, TimeUnit.SECONDS);
		}
		
		return total;
	}

	@Override
	protected long getTotalSize(final File file)
	throws InterruptedException, ExecutionException, TimeoutException {
		final ExecutorService service = Executors.newFixedThreadPool(50);
		try {
			return getTotalSize(service, file);
		} finally {			
			service.shutdown();
		}
	}

	public static void main(final String[] args) 
	throws Exception {
		new NaiveConcurrentDirSizeFinder().run("E:\\Setup");
		new NaiveConcurrentDirSizeFinder().run("E:\\Workspace");
	}	
}