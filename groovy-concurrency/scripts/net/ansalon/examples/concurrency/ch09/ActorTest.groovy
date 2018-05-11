package net.ansalon.examples.concurrency.ch09

import groovyx.gpars.actor.Actors

depp = new HolywoodActor("Johnny Depp").start()
hanks = new HolywoodActor("Tom Hanks").start()

depp.send("Wonka")
hanks.send("Lovell")

depp << "Sparrow"
hanks "Gump"

[depp, hanks]*.join(1, java.util.concurrent.TimeUnit.SECONDS)

depp2 = Actors.actor {
	loop(3, { println "Done acting" }) { 
		react { println "Johnny Depp playing the role $it" } 
	}
}

depp2 << "Sparrow"
depp2 << "Wonka"
depp2 << "Scissorhands"
depp2 << "Cesar"

depp2.join(1, java.util.concurrent.TimeUnit.SECONDS)