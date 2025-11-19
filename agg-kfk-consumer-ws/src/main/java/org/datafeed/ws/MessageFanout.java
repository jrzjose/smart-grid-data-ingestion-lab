package org.datafeed.ws;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import org.datafeed.*;
import org.datafeed.models.*;;

@EnableScheduling
@Configuration
public class MessageFanout {
    @Autowired
    SimpMessagingTemplate template;
    
	  @Autowired
    DataConsumption dataConsumption;

    @Scheduled(fixedDelay = 15000)
    public void sendAdhocAggregatedData() {
      if (dataConsumption.hasUpdates())
        this.template.convertAndSend("/topic/agg", dataConsumption.getAggregations());
    }
}