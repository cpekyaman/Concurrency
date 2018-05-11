package net.ansalon.examples.concurrency.ch09

import groovyx.gpars.dataflow.DataflowVariable
import static groovyx.gpars.dataflow.Dataflow.task 

def fetchContent(String url, DataflowVariable content) {
	println("Requesting data from $url")
	content << url.toURL().text
	println("Set content from $url")
}

content1 = new DataflowVariable()
content2 = new DataflowVariable()
task { fetchContent("http://www.agiledeveloper.com", content1) }
task { fetchContent("http://pragprog.com", content2) }

println("Waiting for data to be set")
println("Size of content1 is ${content1.val.size()}")
println("Size of content2 is ${content2.val.size()}")
