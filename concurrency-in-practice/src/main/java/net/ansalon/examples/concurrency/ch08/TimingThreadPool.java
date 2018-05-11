package net.ansalon.examples.concurrency.ch08;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public abstract class TimingThreadPool extends ThreadPoolExecutor {	
	
	public TimingThreadPool(
			int corePoolSize, int maximumPoolSize, long keepAliveTime, 
			TimeUnit unit, BlockingQueue<Runnable> workQueue, 
			ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		
		super(corePoolSize, maximumPoolSize, 
				keepAliveTime, unit, workQueue, threadFactory, handler);
	}

	private final Logger LOG = Logger.getLogger("TimingThreadPool");
	
	private final ThreadLocal <Long > startTime = new ThreadLocal <Long >();
	private final AtomicLong numTasks = new AtomicLong();
	private final AtomicLong totalTime = new AtomicLong();

	@Override
	protected void beforeExecute(Thread t, Runnable r) {		
		super.beforeExecute(t, r);
		LOG.fine (String .format ( "Thread % s: start %s" , t, r ));
		startTime .set ( System .nanoTime ());
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		try {			
			long taskTime = System.nanoTime() - startTime.get();
			numTasks.incrementAndGet();
			totalTime.addAndGet(taskTime);
			
			LOG.fine(String.format("Thread %s: end %s, time =%dns ", t, r, taskTime));
		} finally {
			super.afterExecute(r, t);
		}
	}

	@Override
	protected void terminated() {
		try {
			LOG.info(String.format("Terminated : avg time = %dns ", totalTime.get() / numTasks.get()));
		} finally {
			super.terminated();
		}
	}
}
