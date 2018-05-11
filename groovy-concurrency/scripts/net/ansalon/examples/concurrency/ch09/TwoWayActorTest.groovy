package net.ansalon.examples.concurrency.ch09

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import groovyx.gpars.actor.Actors

fortuneTeller = Actors.actor {
	loop {
		react { name ->
			sender.send("$name, you have a bright future")
		}
	}
}

latch = new CountDownLatch(2)
fortuneTeller.sendAndContinue("Bob") { println it; latch.countDown() }
fortuneTeller.sendAndContinue("Fred") { println it; latch.countDown() }

println "Bob and Fred are keeping their fingers crossed"

if (!latch.await(1, TimeUnit.SECONDS))
	println "Fortune teller didn't respond before timeout!"
else
	println "Bob and Fred are happy campers"
