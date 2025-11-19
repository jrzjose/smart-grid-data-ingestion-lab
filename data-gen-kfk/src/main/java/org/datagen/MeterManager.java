package org.datagen;

import java.util.Random;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.lab.Config;
import org.lab.TimerUtil;

public class MeterManager {
    private static ScheduledExecutorService pool;
    static final int LOCATIONS = 10;
    static final int MIN_METERS = 20;
    static final int MAX_METERS = 25;

    public void start() {
		TimerUtil.block(TimeUnit.MINUTES, 2);
        pool = Executors.newScheduledThreadPool(LOCATIONS * MAX_METERS + 1);
		BlockingQueue<String> queue = new LinkedBlockingQueue<>();
		
		String location = "loc_";
		int meters;
        for(int l=0; l<LOCATIONS; l++) {
			meters = rand(MIN_METERS, MAX_METERS);
			for(int m=0; m<meters; m++) {
				MeterOutput meter = new MeterOutput(location + (l+1), m+1, queue);				
				pool.schedule(meter, m, TimeUnit.MILLISECONDS);
			}
		}

		DataPublisher publisher = new DataPublisher(queue, Config.getKafkaProducer());
		pool.schedule(publisher, 0, TimeUnit.MILLISECONDS);
		System.out.println("*** SCHEDULED ***");
		scheduleStop();
    }

	public void scheduleStop() {
		long millisec = 60 * 60 * 1000;
		Timestamp scheduledTime = new Timestamp(System.currentTimeMillis() + millisec);
		System.out.println("Scheduled shutdown at : " + scheduledTime);
		
		TimerTask stopDataGen = new TimerTask()
		{
			public void run() {
				System.out.println("Shutting down data generation");
				shutdown();
				TimerUtil.block(TimeUnit.SECONDS, 1);
				System.exit(0);
			}
		};

		new Timer("stop broadcasting data...", true).schedule(stopDataGen, millisec);
	}

	public void shutdown() {
		pool.shutdown();

		try {
			if (!pool.awaitTermination(1000*60, TimeUnit.MILLISECONDS)) {
				System.out.println("Unable to gracefully terminate data gen");
			}
			pool.shutdownNow();
		} catch (InterruptedException e) {
			pool.shutdownNow();
		}
	}

	public int rand(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
	}

	public static void main(String[] args) {
		MeterManager manager = new MeterManager();
		manager.start();
	}
}
