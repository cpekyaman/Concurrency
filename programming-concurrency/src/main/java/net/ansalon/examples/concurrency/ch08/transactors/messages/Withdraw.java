package net.ansalon.examples.concurrency.ch08.transactors.messages;

public class Withdraw {
	private final Integer amount;

	public Withdraw(Integer amount) {
		this.amount = amount;
	}

	public Integer getAmount() {
		return amount;
	}
}
