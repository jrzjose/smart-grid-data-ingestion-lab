package org.aggregations.avgs;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.kstream.*;

import java.time.Duration;

public class MetersCount {
    private static final String METER_LOC_COUNT_STORE = "meter-loc-count-store";
    private static final String METER_LOC_COUNT_TOPIC = "meter.loc.count.agg";

    public static void setup(final KStream<String, String> stream) {
        final TimeWindows timeWindows = TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(15L));
        final Serde<Windowed<String>> windowedSerde = WindowedSerdes.timeWindowedSerdeFrom(String.class,
                timeWindows.size());

        // think of a use case...
    }
}