package org.lab;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StopScheduler {

	void test() {
		System.out.println("....");
	}
    
	public void scheduleStop(ScheduledExecutorService pool) {
		long millisec = 1 * 1 * 1000;
		Timestamp scheduledTime = new Timestamp(System.currentTimeMillis() + millisec);
		System.out.println("Scheduled shutdown at : " + scheduledTime);
		
		TimerTask stopDataGen = new TimerTask()
		{
			public void run() {
				System.out.println("Shutting down app...");
				shutdown(pool);
				TimerUtil.block(TimeUnit.SECONDS, 1);
				System.exit(0);
			}
		};

		new Timer("app_"+System.currentTimeMillis(), true).schedule(stopDataGen, millisec);
	}

	public void shutdown(ScheduledExecutorService pool) {
		if (pool!=null)
			pool.shutdown();
		else
			return;

		try {
			if (!pool.awaitTermination(1000*60, TimeUnit.MILLISECONDS)) {
				System.out.println("Unable to gracefully terminate app");
			}
			pool.shutdownNow();
		} catch (InterruptedException e) {
			pool.shutdownNow();
		}
	}
	public static void main(String[] args) throws Exception{
		StopScheduler manager = new StopScheduler();
		manager.test();
		Thread.sleep(2000);
	}
}
