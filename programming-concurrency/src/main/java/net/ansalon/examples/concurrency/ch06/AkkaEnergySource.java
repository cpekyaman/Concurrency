package net.ansalon.examples.concurrency.ch06;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import akka.stm.Atomic;
import akka.stm.Ref;

public class AkkaEnergySource {
	public static AkkaEnergySource create() {
		final AkkaEnergySource energySource = new AkkaEnergySource();
		energySource.init();
		return energySource;
	}
	
	private final long MAXLEVEL = 100;
	final Ref<Long> level = new Ref<Long>(MAXLEVEL);
	final Ref<Long> usageCount = new Ref<Long>(0L);
	final Ref<Boolean> keepRunning = new Ref<Boolean>(true);
	private static final ScheduledExecutorService replenishTimer =	Executors.newScheduledThreadPool(10);
	
	private AkkaEnergySource() {
	}

	private void init() {
		replenishTimer.schedule(new Runnable() {
			public void run() {
				replenish();
				if (keepRunning.get())
					replenishTimer.schedule(this, 1, TimeUnit.SECONDS);
			}
		}, 1, TimeUnit.SECONDS);
	}

	public void stop() {
		keepRunning.swap(false);
		replenishTimer.shutdown();
	}
	
	public long getUnitsAvailable() { return level.get(); }
	public long getUsageCount() { return usageCount.get(); }
	
	private void replenish() {
		new Atomic<Object>() {
			public Object atomically() {
				long currentLevel = level.get();
				if (currentLevel < MAXLEVEL)
					level.swap(currentLevel + 1);
				return null;
			}
		}.execute();
	}
	
	public boolean consume(final long units) {
		return new Atomic<Boolean>() {
			public Boolean atomically() {
				long currentLevel = level.get();
				if (units > 0 && currentLevel >= units) {
					level.swap(currentLevel - units);
					usageCount.swap(usageCount.get() + 1);
					return true;
				} else {
					return false;
				}
			}
		}.execute();
	}
}
