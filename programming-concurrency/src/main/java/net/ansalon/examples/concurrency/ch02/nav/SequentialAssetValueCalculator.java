package net.ansalon.examples.concurrency.ch02.nav;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SequentialAssetValueCalculator extends AbstractAssetValueCalculator {

	@Override
	public double computeNetAssetValue(Map<String, Integer> stocks)
	throws ExecutionException, InterruptedException, IOException {
		double assetValue = 0.0;
		for(String ticker : stocks.keySet()) {
			assetValue += stocks.get(ticker) * TickerPriceFinder.getPrice(ticker);
		}
		return assetValue;
	}
	
	public static void main(String[] args) {
		try {
			(new SequentialAssetValueCalculator()).timeAndComputeValue();
		} catch (ExecutionException e) {			
			e.printStackTrace();
		} catch (InterruptedException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
}
