package net.ansalon.examples.concurrency.ch12;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;

public class ConcurrentPutTakeTest {	
	private ExecutorService executorPool = Executors.newCachedThreadPool();
	
	private CyclicBarrier barrier;
	private AtomicInteger putCheckSum;
	private AtomicInteger takeCheckSum;
	private BoundedBuffer<Integer> buffer;
	
	private int iterationCount;
	private int numOfProducers;
	
	public ConcurrentPutTakeTest(int bufferSize, int numOfProducers, int iterationCount) {
		this.buffer = new BoundedBuffer<Integer>(bufferSize);
		this.barrier = new CyclicBarrier(numOfProducers * 2 + 1); // producers == consumers + main thread
		this.putCheckSum = new AtomicInteger(0);
		this.takeCheckSum = new AtomicInteger(0);
		
		this.iterationCount = iterationCount;
		this.numOfProducers = numOfProducers;
	}
	
	public void test() {
		try {
			for (int i=0; i< numOfProducers ; i++) {
				executorPool.execute(new Producer(barrier, putCheckSum, buffer, iterationCount));
				executorPool.execute(new Consumer(barrier, takeCheckSum, buffer, iterationCount));
			}
			
			System.out.println("Starting...");
			barrier.await(); // wait for all threads to be ready
			
			System.out.println("Ending...");
			barrier.await(); // wait for all threads to finish
			
			Assert.assertEquals(putCheckSum.get(), takeCheckSum.get());
		} catch (InterruptedException e) {			
			e.printStackTrace();
		} catch (BrokenBarrierException e) {			
			e.printStackTrace();
		} finally {
			System.out.println("Shutting Down...");
			executorPool.shutdown();
		}
	}
	
	public static void main(String[] args) {
		new ConcurrentPutTakeTest(10, 10, 10000).test();
	}
}
