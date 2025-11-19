package org.lab;

public class KeyUtil {
    public static KafkaKey resolveKey(String key) {
        if (key.contains("@")) {
            String timeframe = key.split("@")[1];
            return KafkaKey.builder()
                .key(key.split("@")[0])
                .startTime(timeframe.split("/")[0])
                .endTime(timeframe.split("/")[1].replace("]", ""))
                .build();
        }

        return KafkaKey.builder()
                .key(key)
                .build();
    }
}
