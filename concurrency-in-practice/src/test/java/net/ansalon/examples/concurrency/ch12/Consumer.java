package net.ansalon.examples.concurrency.ch12;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer extends BufferTestRunner implements Runnable {
	
	public Consumer(
			final CyclicBarrier barrier, 
			final AtomicInteger checkSum,
			final BoundedBuffer<Integer> buffer,
			final int iterations) {
		super(barrier, checkSum, buffer, iterations);
	}
	
	@Override
	public void run() {
		try {			
			barrier.await();
			int sum = 0;
			for (int i = iterations ; i > 0; -- i) {
				sum += buffer.take();
			}
			checkSum.getAndAdd(sum );
			barrier.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (BrokenBarrierException e) {
			throw new RuntimeException(e);
		}
	}
}
