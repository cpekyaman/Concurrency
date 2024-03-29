package net.ansalon.examples.concurrency.ch05;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantEnergySource {
	// to prevent this escaping from constructor
	public static ReentrantEnergySource instance() {
		final ReentrantEnergySource instance = new ReentrantEnergySource();
		instance.init();
		return instance;
	}
	
	// avoid thread.sleep and resource usage
	private static final ScheduledExecutorService replenishTimer = Executors.newScheduledThreadPool(10);
	private ScheduledFuture<?> replenishTask;
	
	private final long MAXLEVEL = 100;
	private long level = MAXLEVEL;
	private long usage = 0;
	private final ReadWriteLock monitor = new ReentrantReadWriteLock();
	
	private ReentrantEnergySource() {}

	public void init() {
		replenishTask = replenishTimer.scheduleAtFixedRate(new Runnable() {
			public void run() {
				replenish();
			}
		}, 0, 1, TimeUnit.SECONDS);
	}

	public long getUnitsAvailable() {
		monitor.readLock().lock();
		try {
			return level;
		} finally {
			monitor.readLock().unlock();
		}
	} 
	
	public long getUsageCount() {
		monitor.readLock().lock();
		try {
			return usage;
		} finally {
			monitor.readLock().unlock();
		}
	} 

	public boolean useEnergy(final long units) {
		monitor.writeLock().lock();
		try {
			if (units > 0 && level >= units) {
				level -= units;
				usage++;
				return true;
			} else {
				return false;
			}
		} finally {
			monitor.writeLock().unlock();
		}
	}

	public synchronized void stopEnergySource() {
		replenishTask.cancel(false);
	}

	private void replenish() {
		monitor.writeLock().lock();
		try {
			if (level < MAXLEVEL) {
				level++;
			}
		} finally {
			monitor.writeLock().unlock();
		}
	}
}
