package org.datafeed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DatafeedApp {

    public static void main(String[] args) {
		SpringApplication.run(DatafeedApp.class, args);
	}

    @Bean
	public CommandLineRunner run(DataConsumption dataConsumption) // move impl to a separate class
    {
        return args -> {
            dataConsumption.start();
        };
    }
}