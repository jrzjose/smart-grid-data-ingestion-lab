package org.lab;

import java.io.File;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;

@Getter
@Setter
@ToString
public class Config {
    private String appId;
    private String topic;
    private Integer locations;
    private Integer minMeters;
    private Integer maxMeters;
    private String bootstrapServers;
    private boolean isKafkaEnabled; // if testing without kafka
    private String groupId;
    private String aggConsumerType;
    private String aggMeterConsumption;
    private String aggLocationConsumption;

    public static Config settings = load();

    static Config loadDefaults() {
        System.out.println("Cfg Loading defaults");
        Config config = new Config();
        
        config.topic = "meter.readings";
        config.locations = 10;
        config.minMeters = 20;
        config.maxMeters = 30;
        // config.bootstrapServers = "127.0.0.1:9092, 127.0.0.1:9094";
        config.bootstrapServers = "kafka1:9093,kafka2:9093";
        config.groupId = "consumer-agg";
        config.isKafkaEnabled = true;
        config.appId = "meter-agg-app1";
        config.aggConsumerType = "customer.type.consumption.agg";
        config.aggMeterConsumption = "meter.consumption.agg";
        config.aggLocationConsumption = "location-consumption.agg";
        return config;
    }

    static Config load() {
        String fileName = getCfgSourceFile();
        if (StringUtils.isEmpty(fileName))
            return loadDefaults();

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        Config config = loadDefaults();

        try {
            config = mapper.readValue(new File(fileName), Config.class);
        } catch (Exception e) {
            System.out.println("Unable to load yaml configuration: " + e.getMessage());
            // e.printStackTrace();
        }
        return config;
    }

    static String getCfgSourceFile() {
        Properties systemProperties = System.getProperties();
        String file = systemProperties.getProperty("cfg");
        return file;
    }

    public static Properties getProperties() {
        // producer properties
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Config.settings.bootstrapServers);
        
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        
        return properties;
    }

    public static KafkaProducer<String, String> getKafkaProducer() {
        // producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.settings.bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "500");
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024));
        return new KafkaProducer<String, String>(properties);
    }

    public static void main(String[] args) {
        Config config = Config.settings;
        System.out.println(config);
    }
}
