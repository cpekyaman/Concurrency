package net.ansalon.examples.concurrency.ch08.typedactors;

import akka.actor.TypedActor;
import akka.transactor.Atomically;
import static akka.transactor.Coordination.coordinate;

public class AccountServiceImpl extends TypedActor implements AccountService {
	@Override
	public void transfer(final Account from, final Account to, final int amount) {
		coordinate(true, new Atomically() {
			public void atomically() {
				to.deposit(amount);
				from.withdraw(amount);
			}
		});
	}
}
