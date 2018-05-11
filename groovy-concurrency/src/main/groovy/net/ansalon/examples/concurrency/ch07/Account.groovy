package net.ansalon.examples.concurrency.ch07

import java.util.concurrent.Callable;

import clojure.lang.LockingTransaction;
import clojure.lang.Ref

class Account {
	private final Ref currBalance
	
	Account(initialBalance) { currBalance = new Ref(initialBalance); println currBalance.class.name }
	
	def getBalance() { currBalance.deref() }
	
	def deposit(amount) {
		println currBalance.class.name
		LockingTransaction.runInTransaction({	
			println currBalance.class.name
			if(amount > 0) {				
				currBalance.set(currBalance.deref() + amount)
				println "deposit ${amount}"
			} else {
				throw new RuntimeException("Invalid Deposit Amount")
			}
		} as Callable)
	}
	
	def withdraw(amount) {
		println currBalance.class.name
		LockingTransaction.runInTransaction({	
			println currBalance.class.name
			if(amount > 0 && currBalance.deref() >= amount) {				
				currBalance.set(currBalance.deref() - amount)
				println "withdraw ${amount}"
			} else {
				throw new RuntimeException("Invalid Withdraw Amount")
			}
		} as Callable)
	}
}
