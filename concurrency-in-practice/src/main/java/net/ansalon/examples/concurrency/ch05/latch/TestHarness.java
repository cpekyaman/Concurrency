package net.ansalon.examples.concurrency.ch05.latch;

import java.util.concurrent.CountDownLatch;

public class TestHarness {
	private final Task task;
	private final int threadCount;	
	
	private TestHarness(final int threadCount, final Task task) {
		this.threadCount = threadCount;
		this.task = task;
	}
	
	public long run() 
	throws InterruptedException {
		final CountDownLatch startGate = new CountDownLatch(1);
		final CountDownLatch endGate = new CountDownLatch (threadCount);	
		
		createThreads(startGate, endGate);
		
		long start = System.nanoTime();
		startGate.countDown();
		endGate.await();
		long end = System .nanoTime ();
		return end - start;
	}
	
	private void createThreads(final CountDownLatch startGate, final CountDownLatch endGate) {
		for(int i = 0; i<threadCount; ++i) {
			(newTask(startGate, endGate)).start();
		}
	}
	
	private Thread newTask(final CountDownLatch startGate, final CountDownLatch endGate) {
		return new Thread() {
			public void run() {
				try {
					startGate.await();
					try {
						task.run();
					} finally {
						endGate.countDown();
					}
				} catch (InterruptedException ignored) {}
			}
		};
	}
	
	public static void main(String[] args) {
		try {
			long duration = (new TestHarness(20, new CounterTask(20000))).run();
			System.out.println(duration);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
	}
}
