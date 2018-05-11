package net.ansalon.examples.concurrency.ch04;

public class MutablePoint {
	
	public MutablePoint (MutablePoint point) { this(point.x, point.y);}
	
	public MutablePoint () { x = 0; y = 0; }
	
	public MutablePoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int x,y;	
}
