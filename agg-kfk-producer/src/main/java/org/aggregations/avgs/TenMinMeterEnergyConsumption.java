package org.aggregations.avgs;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.*;
import org.lab.KafkaKey;
import org.lab.KeyUtil;

import com.google.gson.JsonParser;

import java.time.Duration;

public class TenMinMeterEnergyConsumption {
    private static final String METER_USAGE_STORE = "meter-consumption-store";
    private static final String METER_USAGE_TOPIC = "meter.consumption.agg";
    public static void setup(final KStream<String, String> stream) {
        final TimeWindows timeWindows = TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(10L));
        final Serde<Windowed<String>> windowedSerde = WindowedSerdes.timeWindowedSerdeFrom(String.class, timeWindows.size());
        final Serde<Long> longSerde = Serdes.Long();

        stream
            .mapValues(value -> {
                return JsonParser.parseString(value)
                        .getAsJsonObject().get("consumption_value")
                        .getAsLong();
            })
            .groupByKey()
            .windowedBy(timeWindows)
            .aggregate(
                () -> 0L, // Initializer: initial sum is 0.0
                (aggKey, newValue, aggValue) -> { 
                    // System.out.println("aggKey:"+aggKey + " aggValue:"+aggValue ); 
                    return aggValue + newValue;
                },
                Materialized.as(METER_USAGE_STORE).with(Serdes.String(), longSerde)
            )
            .toStream()
            .mapValues((key, value) -> {
                KafkaKey kfkKey = KeyUtil.resolveKey(key.toString());
                return "{" +
                        "meter_id:'" + key.key() +
                        "', start_time:" + kfkKey.getStartTime() + 
                        ", end_time:"  + kfkKey.getEndTime() +
                        ", consumption_value:" + value +
                        "}";
            })
            .to(METER_USAGE_TOPIC, Produced.with(windowedSerde, Serdes.String()));
    }
}