package org.aggregations.avgs;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.*;
import org.lab.KafkaKey;
import org.lab.KeyUtil;

import com.google.gson.JsonParser;

import java.time.Duration;

public class TenMinLocationEnergyConsumption {
    private static final String LOCATION_CONSUMPTION_STORE = "location-consumption-store";
    private static final String LOCATION_CONSUMPTION_TOPIC = "location-consumption.agg";

    public static void setup(final KStream<String, String> stream) {
        final TimeWindows timeWindows = TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(10L));
        final Serde<Windowed<String>> windowedSerde = WindowedSerdes.timeWindowedSerdeFrom(String.class, timeWindows.size());
        final Serde<Long> longSerde = Serdes.Long();

        stream
            .selectKey((k, value) -> {
                try {
                    // System.out.println(value);
                    return JsonParser.parseString(value)
                        .getAsJsonObject().get("location_id")
                        .getAsString();
                } 
                catch (Exception e) {
                    e.printStackTrace();
                    return "no_loc";
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
                Materialized.as(LOCATION_CONSUMPTION_STORE).with(Serdes.String(), longSerde)
            )
            .toStream()
            .mapValues((key, value) -> {
                KafkaKey kfkKey = KeyUtil.resolveKey(key.toString());
                return "{" +
                        "location_id:'" + key.key() +
                        "', start_time:" + kfkKey.getStartTime() + 
                        ", end_time:"  + kfkKey.getEndTime() +
                        ", consumption_value:" + value.toString() +
                        "}";
            })
            .to(LOCATION_CONSUMPTION_TOPIC, Produced.with(windowedSerde, Serdes.String()));
    }
}