package net.ansalon.examples.concurrency.ch05.latch;

public class CounterTask implements Task {
	private final long limit;
	
	public CounterTask(long limit) {
		this.limit = limit;
	}
	
	@Override
	public void run() {
		System.out.println("Counter started !!!");
		long count = 0;
		for(long i = 0; i<limit; ++i) {
			++count;
		}
		System.out.println("Counter ended !!!");
	}
}
