package net.ansalon.examples.concurrency.ch05.cache;

public interface Computable <A, V> {
	V compute(A arg ) throws InterruptedException;
}
