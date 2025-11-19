package org.datafeed.ws;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.datafeed.*;
import org.datafeed.models.*;;

@Controller
public class AggController {

	@Autowired
    DataConsumption dataConsumption;

	@MessageMapping("/agg-upd")
	@SendTo("/topic/agg")
	public Aggregations getAggregations(String ping) throws Exception {
		return dataConsumption.getAggregations();
	}
}
