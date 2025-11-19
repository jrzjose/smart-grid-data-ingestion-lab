package org.datagen;

import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.lab.Config;

import com.google.gson.JsonParser;
import com.google.gson.JsonElement;

public class DataPublisher implements Runnable {
    private BlockingQueue<String> meterData;

    // create kafka producer
    KafkaProducer<String, String> producer;

    public DataPublisher(BlockingQueue<String> meterData, KafkaProducer<String, String> producer) {
        this.meterData = meterData;
        this.producer = producer;

        System.out.println("broadcasting meter data...");
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (!meterData.isEmpty()) {
                    System.out.println("polling...");
                    String data = meterData.poll(TimeUnit.MILLISECONDS.toNanos(250), TimeUnit.NANOSECONDS);
                    String id = null;
                    try {
                        JsonElement jsonObj = JsonParser.parseString(data);
                        
                        id = jsonObj.getAsJsonObject().get("location_id").getAsString() + "_" +
                                jsonObj.getAsJsonObject().get("meter_id").getAsString();
                    }
                    finally {
                        if (id == null)
                            id = "" + System.currentTimeMillis();
                    }

                    if (producer != null){
                        producer.send(new ProducerRecord<String, String>(Config.settings.getTopic(), id, data));
                    }
                    else
                    {
                        System.out.println(data);
                    }
                }
                else {
                    System.out.println("waiting for meter data...");
                    Thread.sleep(1000*30);
                }
            } 
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("something went wrong!!");
                break;
            }
        }
    }

}
