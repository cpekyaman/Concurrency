package net.ansalon.examples.concurrency.ch08.transactors.actors;

import net.ansalon.examples.concurrency.ch08.transactors.messages.*;
import akka.stm.Ref;
import akka.transactor.UntypedTransactor;

public class Account extends UntypedTransactor {
	private final Ref<Integer> balance = new Ref<Integer>(0);

	@Override
	public void atomically(final Object message) throws Exception {
		if (message instanceof Deposit) {
			deposit((Deposit)message);
		} else if (message instanceof Withdraw) {
			withdraw((Withdraw)message);
		} else if (message instanceof FetchBalance) {
			balance();
		}
	}

	private void deposit(Deposit message) {
		int amount = ((Deposit) (message)).getAmount();
		if (amount > 0) {
			balance.swap(balance.get() + amount);
			System.out.println("Received Deposit request " + amount);
		}
	}

	private void withdraw(Withdraw message) {
		int amount = ((Withdraw) (message)).getAmount();
		System.out.println("Received Withdraw request " + amount);
		if (amount > 0 && balance.get() >= amount)
			balance.swap(balance.get() - amount);
		else {
			System.out.println("...insufficient funds...");
			throw new RuntimeException("Insufficient fund");
		}
	}

	private void balance() {
		getContext().tryReply(new Balance(balance.get()));
	}
}
