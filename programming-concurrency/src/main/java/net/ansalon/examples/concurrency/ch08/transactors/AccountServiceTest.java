package net.ansalon.examples.concurrency.ch08.transactors;

import net.ansalon.examples.concurrency.ch08.transactors.actors.Account;
import net.ansalon.examples.concurrency.ch08.transactors.actors.AccountService;
import net.ansalon.examples.concurrency.ch08.transactors.messages.Balance;
import net.ansalon.examples.concurrency.ch08.transactors.messages.Deposit;
import net.ansalon.examples.concurrency.ch08.transactors.messages.FetchBalance;
import net.ansalon.examples.concurrency.ch08.transactors.messages.Transfer;
import akka.actor.ActorRef;
import akka.actor.Actors;

public class AccountServiceTest {
	public static void printBalance(
			final String accountName,
			final ActorRef account) {
		Balance balance = (Balance) (account.ask(new FetchBalance()).get());
		System.out.println(accountName + " balance is " + balance.getAmount());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	throws Exception {
		final ActorRef account1 = Actors.actorOf(Account.class).start();
		final ActorRef account2 = Actors.actorOf(Account.class).start();
		final ActorRef accountService = Actors.actorOf(AccountService.class).start();
		
		account1.tell(new Deposit(1000));
		account2.tell(new Deposit(1000));
		
		Thread.sleep(1000);
		printBalance("Account1", account1);
		printBalance("Account2", account2);
		
		System.out.println("Let's transfer $20... should succeed");
		accountService.tell(new Transfer(account1, account2, 20));
		Thread.sleep(1000);
		printBalance("Account1", account1);
		printBalance("Account2", account2);
		
		System.out.println("Let's transfer $2000... should not succeed");
		accountService.tell(new Transfer(account1, account2, 2000));
		Thread.sleep(6000);
		printBalance("Account1", account1);
		printBalance("Account2", account2);
		
		Actors.registry().shutdownAll();
	}

}
