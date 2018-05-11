package net.ansalon.examples.concurrency.ch06;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EnergyConsumer {
	private static final AkkaEnergySource energySource = AkkaEnergySource.create();
	
	public static void main(String[] args) {
		System.out.println("Energy level at start: "
				+ energySource.getUnitsAvailable());
		List<Callable<Object>> tasks = new ArrayList<Callable<Object>>();
		for (int i = 0; i < 10; i++) {
			tasks.add(new Callable<Object>() {
				public Object call() {
					for (int j = 0; j < 7; j++)
						energySource.consume(1);
					return null;
				}
			});
		}
		
		final ExecutorService service = Executors.newFixedThreadPool(10);
		try {			
			service.invokeAll(tasks);
			System.out.println("Energy level at end: " + energySource.getUnitsAvailable());
			System.out.println("Usage: " + energySource.getUsageCount());			
		} catch (InterruptedException e) {			
			e.printStackTrace();
		} finally {
			energySource.stop();
			service.shutdown();
		}
	}
}
