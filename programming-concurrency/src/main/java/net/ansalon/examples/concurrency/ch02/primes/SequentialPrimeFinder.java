package net.ansalon.examples.concurrency.ch02.primes;

public class SequentialPrimeFinder extends AbstractPrimeFinder {

	@Override
	public int countPrimes(int number) {
		return countPrimesInRange(1, number);
	}

	public static void main(String[] args) {
		(new SequentialPrimeFinder()).timeAndCompute(10000000);
	}
}
