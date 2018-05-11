package net.ansalon.examples.concurrency.ch05;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class FixedEnergySource {
	// to prevent this escaping from constructor
	public static FixedEnergySource instance() {
		final FixedEnergySource instance = new FixedEnergySource();
		instance.init();
		return instance;
	}
	
	// avoid thread.sleep and resource usage
	private static final ScheduledExecutorService replenishTimer = Executors.newScheduledThreadPool(10);
	private ScheduledFuture<?> replenishTask;
	
	private final long MAXLEVEL = 100;
	private final AtomicLong level = new AtomicLong(MAXLEVEL);
	
	private FixedEnergySource() {}

	public void init() {
		replenishTask = replenishTimer.scheduleAtFixedRate(new Runnable() {
			public void run() {
				replenish();
			}
		}, 0, 1, TimeUnit.SECONDS);
	}

	public long getUnitsAvailable() {
		return level.get();
	}

	public boolean useEnergy(final long units) {
		final long currentLevel = level.get();
		if (units > 0 && currentLevel >= units) {
			return level.compareAndSet(currentLevel, currentLevel - units);
		}
		return false;
	}

	public synchronized void stopEnergySource() {
		replenishTask.cancel(false);
		replenishTimer.shutdown();
	}

	private void replenish() {
		if (level.get() < MAXLEVEL) level.incrementAndGet();
	}
}
