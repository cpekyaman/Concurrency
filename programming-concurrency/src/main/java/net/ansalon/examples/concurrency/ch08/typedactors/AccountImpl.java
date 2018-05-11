package net.ansalon.examples.concurrency.ch08.typedactors;

import akka.actor.TypedActor;
import akka.stm.Ref;

public class AccountImpl extends TypedActor implements Account {
	private final Ref<Integer> balance = new Ref<Integer>(0);

	public int getBalance() {
		return balance.get();
	}

	public void deposit(final int amount) {
		if (amount > 0) {
			balance.swap(balance.get() + amount);
			System.out.println("Received Deposit request " + amount);
		}
	}

	public void withdraw(final int amount) {
		System.out.println("Received Withdraw request " + amount);
		if (amount > 0 && balance.get() >= amount)
			balance.swap(balance.get() - amount);
		else {
			System.out.println("...insufficient funds...");
			throw new RuntimeException("Insufficient fund");
		}
	}
}
