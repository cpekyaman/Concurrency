package net.ansalon.examples.concurrency.ch08.transactors.messages;

public class Deposit {
	private final Integer amount;

	public Deposit(Integer amount) {
		this.amount = amount;
	}

	public Integer getAmount() {
		return amount;
	}
}
