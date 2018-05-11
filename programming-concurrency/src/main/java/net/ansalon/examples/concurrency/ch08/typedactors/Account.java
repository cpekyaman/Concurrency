package net.ansalon.examples.concurrency.ch08.typedactors;

import akka.transactor.annotation.Coordinated;

public interface Account {
	int getBalance();
	@Coordinated void deposit(final int amount);
	@Coordinated void withdraw(final int amount);
}
