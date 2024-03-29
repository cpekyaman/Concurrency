package net.ansalon.examples.concurrency.ch04.coordination;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinDirSizeFinder extends AbstractDirSizeFinder {
	private final static ForkJoinPool forkJoinPool = new ForkJoinPool();

	private static class FileSizeFinder extends RecursiveTask<Long> {
		private static final long serialVersionUID = 1L;
		
		final File file;

		public FileSizeFinder(final File theFile) {
			file = theFile;
		}

		@Override
		public Long compute() {
			long size = 0;
			if (file.isFile()) {
				size = file.length();
			} else {
				final File[] children = file.listFiles();
				if (children != null) {
					List<ForkJoinTask<Long>> tasks = new ArrayList<ForkJoinTask<Long>>();
					for (final File child : children) {
						if (child.isFile()) {
							size += child.length();
						} else {
							tasks.add(new FileSizeFinder(child));
						}
					}
					for (final ForkJoinTask<Long> task : invokeAll(tasks)) {
						size += task.join();
					}
				}
			}
			return size;
		}
	}
	
	@Override
	protected long getTotalSize(final File file) {
		return forkJoinPool.invoke(new FileSizeFinder(file));
	}
	
	public static void main(String[] args) 
	throws Exception {
		new ForkJoinDirSizeFinder().run("E:\\Setup");
		new ForkJoinDirSizeFinder().run("E:\\Workspace");
	}
}
