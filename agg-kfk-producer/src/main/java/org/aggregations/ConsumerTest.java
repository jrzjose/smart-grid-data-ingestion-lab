package org.aggregations;

import org.aggregations.avgs.TenMinLocationEnergyConsumption;
import org.aggregations.avgs.TenMinMeterEnergyConsumption;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.StreamsException;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import org.lab.Config;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.common.serialization.StringDeserializer;

public class ConsumerTest {
    private static final Logger log = LoggerFactory.getLogger(MeterAggregator.class);
    private static final Properties properties;
    static {
        properties = Config.getProperties();
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, Config.settings.getGroupId());
    }

    public static void main(String[] args) {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        final CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread("consumer-shutdown-hook") {
            @Override
            public void run() {
                System.out.println("terminating aggregator...");
                consumer.close();
                latch.countDown();
            }
        });

        consumer.subscribe(Arrays.asList(Config.settings.getTopic()));

        while (true) {
            log.info("Polling...");

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(20000));

            int recordCount = records.count();

            log.info("Received: " + recordCount + " records (s)");

            String id;
            for (ConsumerRecord<String, String> record : records) {
                try {
                    JsonElement el = JsonParser.parseString(record.value());

                    log.info("message:" + el);
                } catch (Exception e) {
                }
            }

            // commit offsets after the batch is consumed
            consumer.commitSync(); // or kfkConsumer.commitAsync();
            log.info("Offsets have been committed...");
        }
    }
}