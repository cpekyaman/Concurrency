package net.ansalon.examples.concurrency.ch12;

import org.junit.Test;

import junit.framework.TestCase;

public class BoundedBufferTests extends TestCase {
	
	private static final int LOCKUP_DETECT_TIMEOUT = 2000;
	
	@Test
	public void testIsEmptyWhenConstructed() {
		BoundedBuffer<Integer> bb = new BoundedBuffer<Integer>(10);
		assertTrue(bb.isEmpty());
		assertFalse(bb.isFull());
	}

	@Test
	public void testIsFullAfterPuts() 
	throws InterruptedException {
		BoundedBuffer<Integer> bb = new BoundedBuffer<Integer>(10);
		for (int i = 0; i < 10; i++)
			bb.put(i);
		
		assertTrue(bb.isFull());
		assertFalse(bb.isEmpty());
	}
	
	@Test
	public void testTakeBlocksWhenEmpty() {
		final BoundedBuffer<Integer> bb = new BoundedBuffer<Integer>(10);
		Thread taker = new Thread() {
			public void run() {
				try {
					bb.take();
					fail(); // if we get here , it’ s an error
				} catch (InterruptedException success) {
					// we will block and interrupted by main thread. OK.
				}
			}
		};
		try {
			taker.start();
			Thread.sleep(LOCKUP_DETECT_TIMEOUT); // allow enough time
			taker.interrupt(); // thread is blocked, we're interrupting
			taker.join(LOCKUP_DETECT_TIMEOUT);
			assertFalse(taker.isAlive()); // thread should answer interruption
		} catch (Exception unexpected) {
			fail(); // something unrelated to interruption
		}
	}
}
