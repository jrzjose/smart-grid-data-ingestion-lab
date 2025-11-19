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
        final Serde<Long> longSerde = Serdes.Long();

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
                        .getAsLong();
            })
            .groupByKey(Grouped.with(Serdes.String(), Serdes.Long()))
            .windowedBy(timeWindows)
            .aggregate(
                () -> 0L, // Initializer: initial sum is 0.0
                (aggKey, newValue, aggValue) -> { 
                    // System.out.println("aggKey:"+aggKey + " aggValue:"+aggValue ); 
                    return aggValue + newValue;
                },
                Materialized.as(CUSTOMER_TYPE_CONSUMPTION_STORE).with(Serdes.String(), longSerde)
            )
            .toStream()
            .mapValues((key, value) -> {
                KafkaKey kfkKey = KeyUtil.resolveKey(key.toString());
                return "{" +
                        "customerType:'" + key.key() +
                        "', start_time:" + kfkKey.getStartTime() + 
                        ", end_time:"  + kfkKey.getEndTime() +                
                        ", energy_consumption:" + value +
                        "}";
            })
            .to(CUSTOMER_TYPE_CONSUMPTION_TOPIC, Produced.with(windowedSerde, Serdes.String()));

    }
}