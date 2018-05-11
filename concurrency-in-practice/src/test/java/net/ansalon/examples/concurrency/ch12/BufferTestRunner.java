package net.ansalon.examples.concurrency.ch12;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BufferTestRunner {
	protected final CyclicBarrier barrier;
	protected final AtomicInteger checkSum;
	protected final BoundedBuffer<Integer> buffer;
	protected final int iterations;
	
	protected int xorShift (int y ) {
		y^= (y<< 6);
		y ^= ( y >>> 21);
		y^= (y<< 7);
		return y;
	}
	
	public BufferTestRunner(
			final CyclicBarrier barrier, 
			final AtomicInteger checkSum,
			final BoundedBuffer<Integer> buffer,
			final int iterations) {
		
		this.barrier = barrier;
		this.checkSum = checkSum;
		this.buffer = buffer;
		this.iterations = iterations;
	}
}
