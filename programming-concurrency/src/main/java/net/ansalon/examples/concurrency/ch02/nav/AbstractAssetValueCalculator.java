package net.ansalon.examples.concurrency.ch02.nav;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public abstract class AbstractAssetValueCalculator {
	
	public static Map<String, Integer> readTickers() 
	throws IOException {
		
		final BufferedReader reader = new BufferedReader(new FileReader("data/stocks.txt"));
		final Map<String, Integer> stocks = new HashMap<String, Integer>();
		String stockInfo = null;
		
		while ((stockInfo = reader.readLine()) != null) {
			final String[] stockInfoData = stockInfo.split(",");
			final String stockTicker = stockInfoData[0];
			final Integer quantity = Integer.valueOf(stockInfoData[1]);
			stocks.put(stockTicker, quantity);
		}
		
		return stocks;
	}

	public void timeAndComputeValue() 
	throws ExecutionException, InterruptedException, IOException {
		
		final long start = System.nanoTime();
		final Map<String, Integer> stocks = readTickers();
		final double nav = computeNetAssetValue(stocks);
		final long end = System.nanoTime();
		final String value = new DecimalFormat("$##,##0.00").format(nav);
		
		System.out.println("Your net asset value is " + value);
		System.out.println("Time (seconds) taken " + (end - start) / 1.0e9);
	}

	public abstract double computeNetAssetValue(final Map<String, Integer> stocks) 
	throws ExecutionException, InterruptedException, IOException;
	
	public static void main(String[] args) {
		try {
			final Map<String, Integer> stocks = readTickers();
			for (String entry : stocks.keySet()) {
				System.out.println(entry + ": " + stocks.get(entry));
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
}
