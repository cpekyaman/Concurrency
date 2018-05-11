package net.ansalon.examples.concurrency.ch08.transactors.messages;

public class Balance {
	private final Integer amount;

	public Balance(Integer amount) {
		this.amount = amount;
	}

	public Integer getAmount() {
		return amount;
	}
}
