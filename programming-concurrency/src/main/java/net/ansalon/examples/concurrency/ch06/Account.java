package net.ansalon.examples.concurrency.ch06;

import akka.stm.Atomic;
import akka.stm.Ref;

public class Account {
	private final Ref<Integer> balance = new Ref<Integer>();

	public Account(int initialBalance) {
		balance.swap(initialBalance);
	}

	public int getBalance() {
		return balance.get();
	}

	public void deposit(final int amount) {
		new Atomic<Boolean>() {
			public Boolean atomically() {
				System.out.println("Deposit " + amount);
				
				if (amount > 0) {
					balance.swap(balance.get() + amount);
					return true;
				}
				throw new RuntimeException("Deposit Operation Failed");
			}
		}.execute();
	}

	public void withdraw(final int amount) {
		new Atomic<Boolean>() {
			public Boolean atomically() {	
				System.out.println("Withdraw " + amount);
				
				if (amount > 0 && balance.get() >= amount) {
					balance.swap(balance.get() - amount);
					return true;
				}
				throw new RuntimeException("Withdraw Operation Failed");
			}
		}.execute();
	}
}
