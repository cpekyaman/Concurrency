package net.ansalon.examples.concurrency.ch05;

public class BrokenEnergySource {
	private final long MAXLEVEL = 100;
	private long level = MAXLEVEL;
	private boolean keepRunning = true;

	public BrokenEnergySource() {
		new Thread(new Runnable() {
			public void run() {
				replenish();
			}
		}).start();
	}

	public long getUnitsAvailable() {
		return level;
	}

	public boolean useEnergy(final long units) {
		if (units > 0 && level >= units) {
			level -= units;
			return true;
		}
		return false;
	}

	public void stopEnergySource() {
		keepRunning = false;
	}

	private void replenish() {
		while (keepRunning) {
			if (level < MAXLEVEL)
				level++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
			}
		}
	}
}
