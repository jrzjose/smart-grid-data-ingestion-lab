package org.datafeed;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.datafeed.models.Consumption;
import org.datafeed.models.CustomerType;
import org.datafeed.models.Location;
import org.datafeed.models.Meter;
import org.datafeed.models.Aggregations;
import org.lab.Config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class AggConsumer implements Runnable {     
    private static final Properties properties;
    private static final List<String> topics;
    static {
        properties = Config.getProperties();
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, Config.settings.getGroupId());
        properties.setProperty("auto.offset.reset", "earliest");
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); // manual commit

        //topics = Arrays.asList(Config.settings.getTopic());
        topics = Arrays.asList(
                Config.settings.getAggMeterConsumption(),
                Config.settings.getAggConsumerType(),
                Config.settings.getAggLocationConsumption()
            );
    }

    private List<CustomerType> customerTypes;
    private List<Location> locations;
    private Aggregations agg;
    
    public AggConsumer(Aggregations agg) {
        this.agg = agg;
    }

    @Override
    public void run() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        
        consumer.subscribe(topics);

        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Polling...");

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(20000));

            int recordCount = records.count();

            System.out.println("Received: " + recordCount + " records (s)");

            String id;
            for (ConsumerRecord<String, String> record : records) {
                try {
                    JsonElement el = JsonParser.parseString(record.value());
                    Consumption message = Consumption.builder()
                        .startTime(el.getAsJsonObject().get("start_time").getAsLong())
                        .endTime(el.getAsJsonObject().get("end_time").getAsLong())
                        .energyConsumption(el.getAsJsonObject().get("consumption_value").getAsLong())
                        .build();

                    if (record.topic().equals(Config.settings.getAggMeterConsumption())) {
                        Meter meter = new Meter(message, el.getAsJsonObject().get("meter_id").getAsString());
                        agg.getMeters().add(meter);
                        System.out.println("meter: " + meter);
                    }
                    else if (record.topic().equals(Config.settings.getAggLocationConsumption())) {
                        Location loc = new Location(message, el.getAsJsonObject().get("location_id").getAsString());
                        agg.getLocations().add(loc);
                        System.out.println("location:" + loc);
                    }
                    else if (record.topic().equals(Config.settings.getAggConsumerType())) {
                        CustomerType ct = new CustomerType(message, el.getAsJsonObject().get("customerType").getAsString());
                        agg.getCustomerTypes().add(ct);
                        System.out.println("customerType:" + ct);
                    }
                } 
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // commit offsets after the batch is consumed
            consumer.commitSync(); // or kfkConsumer.commitAsync();
            System.out.println("Offsets have been committed...");
        }
        consumer.close();
    }
}
