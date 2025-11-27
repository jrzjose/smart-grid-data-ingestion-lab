package org.datafeed;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.datafeed.models.Aggregations;
import org.datafeed.models.CustomerType;
import org.datafeed.models.Location;
import org.datafeed.models.Meter;
import org.lab.TimerUtil;

import org.springframework.stereotype.Component;

@Component
public class DataConsumption {
    private Aggregations agg;
    private Map<String, CustomerType> customerTypes;
    private Map<String, Location> locations;
    private Map<String, Meter> meters;

    int aggHash;

    public DataConsumption() {
        agg = new Aggregations();
        customerTypes = Collections.synchronizedMap(new HashMap<>());
        locations = Collections.synchronizedMap(new HashMap<>());
        meters = Collections.synchronizedMap(new HashMap<>());
        aggHash = hashCode();
    }

    void start() {
        Thread t = new Thread(new AggConsumer(customerTypes, locations, meters));
        t.start();
    }

    public boolean hasUpdates() {
        if (aggHash != hashCode()) {
            aggHash = hashCode();
            return true;
        }

        return false;
    }

    public Aggregations getAggregations() {
        return new Aggregations(customerTypes.values(), locations.values(), meters.values());
    }

    public static void main(String[] args) throws Exception {
        DataConsumption dc = new DataConsumption();
        dc.start();
        TimerUtil.block(TimeUnit.SECONDS, 300);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((customerTypes == null) ? 0 : customerTypes.hashCode());
        result = prime * result + ((locations == null) ? 0 : locations.hashCode());
        result = prime * result + ((meters == null) ? 0 : meters.hashCode());
        return result;
    }    
}
