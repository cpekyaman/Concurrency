package net.ansalon.examples.concurrency.ch06;

import akka.stm.Atomic;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class AccountService {
	public void transfer(final Account from, final Account to, final int amount) {
		new Atomic<Boolean>() {
			public Boolean atomically() {
				System.out.println("Attempting transfer...");
				to.deposit(amount);
				
				delay();
				
				System.out.println("Uncommitted balance after deposit $" + to.getBalance());
				from.withdraw(amount);
				
				return true;
			}
		}.execute();
	}
	
	private void delay() {
		System.out.println("Simulating a delay in transfer...");
		try {
			Thread.sleep(5000);
		} catch (Exception ex) {
		}
	}
	
	public static void testTransfer(
			final Account from, final Account to, final int amount) {
		boolean result = true;
		try {
			new AccountService().transfer(from, to, amount);
		} catch (RuntimeException ex) {
			System.out.println(ex.getMessage());
			result = false;
		}
		
		System.out.println("Result of transfer is " + (result ? "Pass" : "Fail"));
		System.out.println("From account has $" + from.getBalance());
		System.out.println("To account has $" + to.getBalance());
	}
	
	public static void main(String[] args) {
		final Account from = new Account(2000);
		final Account to = new Account(100);

		final ExecutorService service = Executors.newSingleThreadExecutor();
		service.submit(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (Exception ex) {
				}
				to.deposit(20);
			}
		});
		service.shutdown();

		System.out.println("Making normal transfer...");
		testTransfer(from, to, 500);
		System.out.println("Making large transfer...");
		testTransfer(from, to, 5000);
	}
}
