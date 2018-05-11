package net.ansalon.examples.concurrency.ch08.typedactors;

import akka.actor.Actors;
import akka.actor.TypedActor;

public class AccountTransferTest {
	public static void main(String[] args) throws Exception {
		final Account account1
			= TypedActor.newInstance(Account.class, AccountImpl.class);
		final Account account2 
			= TypedActor.newInstance(Account.class, AccountImpl.class);
		final AccountService accountService 
			= TypedActor.newInstance(AccountService.class, AccountServiceImpl.class);
		
		account1.deposit(1000);
		account2.deposit(1000);
		System.out.println("Account1 balance is " + account1.getBalance());
		System.out.println("Account2 balance is " + account2.getBalance());
		
		System.out.println("Let's transfer $20... should succeed");
		accountService.transfer(account1, account2, 20);
		Thread.sleep(1000);
		System.out.println("Account1 balance is " + account1.getBalance());
		System.out.println("Account2 balance is " + account2.getBalance());
		
		System.out.println("Let's transfer $2000... should not succeed");		
		accountService.transfer(account1, account2, 2000);
		Thread.sleep(2000);
		System.out.println("Account1 balance is " + account1.getBalance());
		System.out.println("Account2 balance is " + account2.getBalance());
		
		Actors.registry().shutdownAll();
	}
}
