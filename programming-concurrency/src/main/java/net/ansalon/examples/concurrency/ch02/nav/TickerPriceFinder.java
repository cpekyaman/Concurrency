package net.ansalon.examples.concurrency.ch02.nav;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class TickerPriceFinder {
	public static double getPrice(final String ticker) 
	throws IOException {
		final URL url = new URL("http://ichart.finance.yahoo.com/table.csv?s=" + ticker);
		final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		
		// Date,Open,High,Low,Close,Volume,Adj Close
		// 2011-03-17,336.83,339.61,330.66,334.64,23519400,334.64
		final String discardHeader = reader.readLine();
		final String data = reader.readLine();
		final String[] dataItems = data.split(",");
		return Double.valueOf(dataItems[dataItems.length - 1]);
	}
}
