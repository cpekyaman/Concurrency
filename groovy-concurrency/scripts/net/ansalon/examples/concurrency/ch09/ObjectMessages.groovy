package net.ansalon.examples.concurrency.ch09

import groovyx.gpars.actor.DynamicDispatchActor
import java.util.concurrent.TimeUnit

@groovy.transform.Immutable class LookUp {
	String ticker
}

@groovy.transform.Immutable class Buy {
	String ticker
	int quantity
}

trader = DynamicDispatchActor.become({
	when { Buy message -> 
		println "Buying ${message.quantity} shares of ${message.ticker}" 
	}
	when { LookUp message ->
		sender.send((int)(Math.random() * 1000))
	}
} as groovy.lang.Closure).start()

trader.sendAndContinue(new LookUp("XYZ")) { println "Price of XYZ sock is $it" }
trader << new Buy("XYZ", 200)

trader.join(1, java.util.concurrent.TimeUnit.SECONDS)
