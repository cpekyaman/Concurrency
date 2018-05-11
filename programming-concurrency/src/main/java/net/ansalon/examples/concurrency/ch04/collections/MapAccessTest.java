package net.ansalon.examples.concurrency.ch04.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapAccessTest {
	private static void useMap(final Map<String, Integer> map) {
		try {
			map.put("Cenk", 1);
			map.put("Hasan", 2);
			System.out.println("map size: " + map.size());
			
			for (String key : map.keySet()) {
				System.out.println(key + " : " + map.get(key));
				map.put("Ali", 3);
			}
			
			System.out.println("map size: " + map.size());
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		useMap(new HashMap<String, Integer>());
		useMap(new ConcurrentHashMap<String, Integer>());
	}
}
