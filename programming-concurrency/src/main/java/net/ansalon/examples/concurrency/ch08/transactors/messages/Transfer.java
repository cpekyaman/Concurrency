package net.ansalon.examples.concurrency.ch08.transactors.messages;

import akka.actor.ActorRef;

public class Transfer {
	public final ActorRef from;
	public final ActorRef to;
	public final int amount;

	public Transfer(
			final ActorRef fromAccount, 
			final ActorRef toAccount,
			final int theAmount) {
		from = fromAccount;
		to = toAccount;
		amount = theAmount;
	}
}
