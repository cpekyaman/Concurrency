package net.ansalon.examples.concurrency.ch07;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

// flags don't work with blocking method calls
// interrupt works with interruptible blocking calls
public class BlockingPrimeGenerator {	
	public static void main(String[] args) {
		BlockingQueue<BigInteger> primes = new LinkedBlockingQueue<BigInteger>(1000);

		BlockingPrimeProducer producer = new BlockingPrimeProducer(primes);
		BlockingPrimeConsumer consumer = new BlockingPrimeConsumer(primes);
		
		producer.start();
		consumer.start();
		
		try {
			TimeUnit.SECONDS.sleep(1);
			consumer.cancel();
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
		
		try {
			TimeUnit.SECONDS.sleep(1);
			producer.cancel();
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}		
	}
}

// generate primes
class BlockingPrimeProducer extends Thread {
	private final BlockingQueue<BigInteger> queue;
	private volatile boolean cancelled = false;

	BlockingPrimeProducer(BlockingQueue<BigInteger> queue) {
		this.queue = queue;
	}

	public void run() {
		try {
			BigInteger p = BigInteger.ONE;
			while (! cancelled && ! Thread.currentThread().isInterrupted())
				queue.put(p = p.nextProbablePrime());
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}

	public void cancel() {
		cancelled = true;
		interrupt();
	}
}

// consumer primes slowly
class BlockingPrimeConsumer extends Thread {
	private final BlockingQueue<BigInteger> queue;
	private volatile boolean cancelled = false;

	BlockingPrimeConsumer(BlockingQueue<BigInteger> queue) {
		this.queue = queue;
	}

	public void run() {
		try {			
			while (! cancelled && ! Thread.currentThread().isInterrupted()) {
				TimeUnit.MILLISECONDS.sleep(20);
				System.out.println(queue.take());
			}				
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}

	public void cancel() {
		cancelled = true;
		interrupt();
	}
}
