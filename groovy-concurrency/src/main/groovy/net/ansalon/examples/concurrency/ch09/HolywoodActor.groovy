package net.ansalon.examples.concurrency.ch09

import groovyx.gpars.actor.DefaultActor;

class HolywoodActor extends DefaultActor {
	private def name
	
	def HolywoodActor(name) { this.name = name }
	
	void act() {
		loop {
			react { role ->
				println "$name playing the role $role"
				println "$name runs in ${Thread.currentThread()}"
			}
		}
	} 
}
