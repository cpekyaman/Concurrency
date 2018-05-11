package net.ansalon.examples.concurrency.ch07;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CancellationPrimeGenerator {	
	public static void main(String[] args) {		
		PrimeGenerator generator = new PrimeGenerator();
		new Thread(generator).start();
		try{
			TimeUnit.SECONDS.sleep(1);
		} catch(InterruptedException ie) {
			ie.printStackTrace();
		} finally {
			generator.cancel();
		}
		for(BigInteger prime : generator.getPrimes()) {
			System.out.println(prime);
		}
	}
}

class PrimeGenerator implements Runnable {
	private List<BigInteger> primes = new ArrayList<BigInteger>();
	private volatile boolean cancelled = false;
	
	@Override
	public void run() {
		BigInteger prime = BigInteger.ONE;
		while(! cancelled) {
			prime = prime.nextProbablePrime();
			synchronized (this) {
				primes.add(prime);
			}
		}
	}
	
	public void cancel() { this.cancelled = true; }
	public List<BigInteger> getPrimes() {
		return Collections.unmodifiableList(primes);
	}
}
