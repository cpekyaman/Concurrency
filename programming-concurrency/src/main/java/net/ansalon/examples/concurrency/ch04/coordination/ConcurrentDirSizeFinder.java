package net.ansalon.examples.concurrency.ch04.coordination;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ConcurrentDirSizeFinder extends AbstractDirSizeFinder {
	class DirInfo {
		final public long size;
		final public List<File> subDirectories;

		public DirInfo(
				final long totalSize,
				final List<File> theSubDirs) {
			size = totalSize;
			subDirectories = Collections.unmodifiableList(theSubDirs);
		}
	}
	
	private DirInfo getDirInfo(final File directory) {		
		long total = 0;
		List<File> subDirs = new ArrayList<File>();
		if(directory.isDirectory()) {			
			for(File child : directory.listFiles()) {
				if(child.isFile()) {
					total += child.length();
				} else {
					subDirs.add(child);
				}
			}
		}
		return new DirInfo(total, subDirs);
	}
	
	@Override
	protected long getTotalSize(final File file)
	throws InterruptedException, ExecutionException, TimeoutException {
		final ExecutorService pool = Executors.newFixedThreadPool(50);
		try {			
			final List<File> toBeSearched = new ArrayList<File>();
			toBeSearched.add(file);
			
			long total = 0;			
			while(! toBeSearched.isEmpty()) {
				final List<Future<DirInfo>> results = new ArrayList<Future<DirInfo>>();
				for(final File dir : toBeSearched) {
					results.add(pool.submit(new Callable<DirInfo>() {					
						public DirInfo call() throws Exception {
							return getDirInfo(dir);
						}
					}));
				}
				toBeSearched.clear();
				
				for(Future<DirInfo> result : results) {
					DirInfo dir = result.get(100, TimeUnit.SECONDS);
					total += dir.size;
					toBeSearched.addAll(dir.subDirectories);
				}
			}
			
			return total;
		} finally {
			pool.shutdown();
		}
	}
	
	public static void main(final String[] args) 
	throws Exception {
		new ConcurrentDirSizeFinder().run("E:\\Setup");
		new ConcurrentDirSizeFinder().run("E:\\Workspace");
	}
}
