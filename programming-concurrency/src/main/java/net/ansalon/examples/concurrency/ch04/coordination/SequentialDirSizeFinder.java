package net.ansalon.examples.concurrency.ch04.coordination;

import java.io.File;

public class SequentialDirSizeFinder extends AbstractDirSizeFinder {
	@Override
	protected long getTotalSize(final File file) {
		if (file.isFile())
			return file.length();
		
		final File[] children = file.listFiles();
		long total = 0;
		if (children != null) {
			for (final File child : children)
				total += getTotalSize(child);
		}			
		return total;
	}

	public static void main(final String[] args)
	throws Exception {
		new SequentialDirSizeFinder().run("E:\\Setup");
		new SequentialDirSizeFinder().run("E:\\Workspace");
	}
}
