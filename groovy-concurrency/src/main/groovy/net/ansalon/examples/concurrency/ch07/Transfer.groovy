package net.ansalon.examples.concurrency.ch07

import clojure.lang.LockingTransaction;
import clojure.lang.Ref

def transfer(from , to, amount) {
	LockingTransaction.runInTransaction({
		to.deposit(amount)
		from.withdraw(amount)		
	})
}

def acc1 = new Account(30)
def acc2 = new Account(15)

println "acc1: ${acc1.getBalance()}, acc2: ${acc2.getBalance()}"
transfer(acc1, acc2, 12)
println "acc1: ${acc1.getBalance()}, acc2: ${acc2.getBalance()}"
