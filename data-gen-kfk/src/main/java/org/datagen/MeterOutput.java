package org.datagen;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class MeterOutput implements Runnable {
    private Meter meter;
    private BlockingQueue<String> meterData;

    public MeterOutput() {
        meterData = null;
        meter = Meter.builder()
                .locationId("loc_1")
                .meterId(1)
                .intervalLength(1)
                .readingSource("S") // S=mart, D=digital
                .build();
    }

    public MeterOutput(String location, int meterId, BlockingQueue<String> meterData) {
        this.meterData = meterData;
        meter = Meter.builder()
                .locationId(location)
                .meterId(meterId)
                .intervalLength(1)
                .readingSource("S") // S=mart, D=digital
                .customerType(meterId%5==0 ? "c" : "r")
                .build();
    }

    @Override
    public void run() {
        System.out.println("** Data gen for meter id:" + meter.getMeterId());
        while (!Thread.currentThread().isInterrupted()) {
            Date timestamp = new Date(System.currentTimeMillis());
            int _reading = reading(meter.getConsumptionValue());
            meter.setIntervalTime(timestamp.getTime());
            meter.setConsumptionValue(_reading);

            try {
                if (meterData != null) {
                    // System.out.println("put...:" + meter.getMeterId());
                    this.meterData.put(meter.toJson());
                }
                else {
                    // no kafka, local test
                    System.out.println(meter.toJson());
                }

                Thread.sleep(1000 * 60); // INTERVAL LENGTH
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private static Random r = new Random();
    private int _spin = 0;

    public int spin() {
        int min = 1;
        int max = 5;
        return r.nextInt((max - min) + 1) + min;
    }

    public int reading(int value) {

        _spin = spin();

        switch (_spin) {
            case 1: // decrease
                value--;
                break;
            case 2: // increase
                value++;
                break;
            case 3:
                value -= 10;
                break;
            case 4:
                value += 10;
                break;
            default:
                // Code block if no case matches
        }

        if (value < 0)
            value = 0;
        return value;
    }

    public static void main(String[] args) {
        MeterOutput s = new MeterOutput();
        s.run();
        System.out.println("Meter broadcast/data-generator started!");
    }
}
