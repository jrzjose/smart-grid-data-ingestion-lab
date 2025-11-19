package org.aggregations;

import org.aggregations.avgs.CustomerTypeEnergyConsumption;
import org.aggregations.avgs.MetersCount;
import org.aggregations.avgs.TenMinLocationEnergyConsumption;
import org.aggregations.avgs.TenMinMeterEnergyConsumption;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.lab.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class MeterAggregator {
    private static final Logger log = LoggerFactory.getLogger(MeterAggregator.class);
    private static final Properties properties;
    static {
        properties = Config.getProperties();
        properties.setProperty("application.id", Config.settings.getAppId());
    }

    public static void main(String[] args) {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> meterReadings = builder.stream(Config.settings.getTopic());

        // Setting up Aggregations
        MetersCount.setup(meterReadings);
        CustomerTypeEnergyConsumption.setup(meterReadings);
        TenMinMeterEnergyConsumption.setup(meterReadings);
        TenMinLocationEnergyConsumption.setup(meterReadings);

        Topology appTopology = builder.build();
        log.info("Topology: {}", appTopology.describe());

        KafkaStreams streams = new KafkaStreams(appTopology, properties);

        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                System.out.println("terminating aggregator...");
                streams.close();
                latch.countDown();
            }
        });

        try {
            System.out.println("aggregating...");
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }
}