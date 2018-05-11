package net.ansalon.examples.concurrency.ch07;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LogService {
	public static LogService instanceFor(Writer writer) {
		return new LogService(writer);
	}
	
	private LogService(Writer writer){
		logWriter = new PrintWriter(writer);
		logQueue = new LinkedBlockingQueue<String>(1000);
		logger = new Logger();
	}
	
	private BlockingQueue<String> logQueue;
	private PrintWriter logWriter;
	private Logger logger;
	
	private boolean isShutdown = false;
	private int pending = 0;
	
	public void start() {logger.start();}
	public void stop() {
		synchronized (this) {
			isShutdown = true;
		}
		logger.interrupt();
	}
	
	public void log(String msg) throws InterruptedException {
		synchronized (this) {
			if(isShutdown) {
				throw new IllegalStateException("Logger is shutdown !!!");
			}
			++pending;
		}
		logQueue.put(msg);
	}

	class Logger extends Thread {
		public void run() {
			try {
				while(true) {
					try{
						synchronized (LogService.this) {
							if(isShutdown && pending == 0) break;
						}
						
						String msg = logQueue.take();
						synchronized (LogService.this) {
							--pending;
						}
						logWriter.println(msg);	
					} catch(InterruptedException ie) {
						ie.printStackTrace();
					}
				}
			} finally {
				logWriter.close();
			}			
		}
	}
}