package net.ansalon.examples.concurrency.ch04;

public class Point {
	private final int x, y;

	public Point(int x, int y) {		
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}	
}
