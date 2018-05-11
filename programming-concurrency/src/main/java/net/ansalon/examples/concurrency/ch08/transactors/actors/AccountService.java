package net.ansalon.examples.concurrency.ch08.transactors.actors;

import java.util.Set;

import net.ansalon.examples.concurrency.ch08.transactors.messages.Deposit;
import net.ansalon.examples.concurrency.ch08.transactors.messages.Transfer;
import net.ansalon.examples.concurrency.ch08.transactors.messages.Withdraw;

import akka.transactor.SendTo;
import akka.transactor.UntypedTransactor;

public class AccountService extends UntypedTransactor {
	@Override
	public Set<SendTo> coordinate(final Object message) 
	throws Exception {
		if(message instanceof Transfer) {
			Set<SendTo> coordinations = new java.util.HashSet<SendTo>();
			Transfer transfer = (Transfer) message;
			coordinations.add(sendTo(transfer.to, new Deposit(transfer.amount)));
			coordinations.add(sendTo(transfer.from, new Withdraw(transfer.amount)));
			return java.util.Collections.unmodifiableSet(coordinations);
		}
		return nobody();
	}
	
	@Override
	public void atomically(final Object message) 
	throws Exception {
		
	}	
}
