package org.aggregations.avgs;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.*;
import org.lab.KafkaKey;
import org.lab.KeyUtil;

import com.google.gson.JsonParser;

import java.time.Duration;

public class CustomerTypeEnergyConsumption {
    private static final String CUSTOMER_TYPE_CONSUMPTION_STORE = "customer_type_consumption-store";
    private static final String CUSTOMER_TYPE_CONSUMPTION_TOPIC = "customer.type.consumption.agg";

    public static void setup(final KStream<String, String> stream) {
        final TimeWindows timeWindows = TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(15L));
        final Serde<Windowed<String>> windowedSerde = WindowedSerdes.timeWindowedSerdeFrom(String.class, timeWindows.size());
        final Serde<Double> doubleSerde = Serdes.Double();

        stream
            .selectKey((k, value) -> {
                try {
                    return JsonParser.parseString(value)
                        .getAsJsonObject().get("customerType")
                        .getAsString();
                } 
                catch (Exception e) {
                    e.printStackTrace();
                    return "no_type";
                }
            })
            .mapValues(value -> {
                return JsonParser.parseString(value)
                        .getAsJsonObject().get("consumption_value")
                        .getAsDouble();
            })
            .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
            .windowedBy(timeWindows)
            .aggregate(
                () -> 0D, // Initializer: initial sum is 0.0
                (aggKey, newValue, aggValue) -> { 
                    // System.out.println("aggKey:"+aggKey + " aggValue:"+aggValue ); 
                    return aggValue + newValue;
                },
                Materialized.as(CUSTOMER_TYPE_CONSUMPTION_STORE).with(Serdes.String(), doubleSerde)
            )
            .toStream()
            .mapValues((key, value) -> {
                KafkaKey kfkKey = KeyUtil.resolveKey(key.toString());
                return "{" +
                        "customerType:'" + key.key() +
                        "', start_time:" + kfkKey.getStartTime() + 
                        ", end_time:"  + kfkKey.getEndTime() +                
                        ", consumption_value:" + value +
                        "}";
            })
            .to(CUSTOMER_TYPE_CONSUMPTION_TOPIC, Produced.with(windowedSerde, Serdes.String()));

    }
}