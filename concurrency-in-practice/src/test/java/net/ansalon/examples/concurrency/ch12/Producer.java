package net.ansalon.examples.concurrency.ch12;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer extends BufferTestRunner implements Runnable {
	
	public Producer(
			final CyclicBarrier barrier, 
			final AtomicInteger checkSum,
			final BoundedBuffer<Integer> buffer,
			final int iterations) {
		super(barrier, checkSum, buffer, iterations);
	}
	
	@Override
	public void run() {		
		try {
			int seed = (this.hashCode() ^ (int)System.nanoTime());
			int sum = 0;
			barrier.await();
			for (int i = iterations; i > 0; --i) {
				buffer.put(seed);
				sum += seed;
				seed = xorShift(seed);
			}
			checkSum.getAndAdd(sum);
			barrier.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (BrokenBarrierException e) {
			throw new RuntimeException(e);
		}
	}
}
