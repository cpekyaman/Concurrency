package net.ansalon.examples.concurrency.ch08.typedactors;

public interface AccountService {
	void transfer(final Account from, final Account to, final int amount);
}
