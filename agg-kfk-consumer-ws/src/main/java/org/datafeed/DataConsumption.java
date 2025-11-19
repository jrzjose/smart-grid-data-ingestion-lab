package org.datafeed;

import java.util.concurrent.TimeUnit;
import org.datafeed.models.Aggregations;
import org.lab.TimerUtil;

import org.springframework.stereotype.Component;

@Component
public class DataConsumption {
    private Aggregations agg;
    int aggHash;

    public DataConsumption() {
        agg = new Aggregations();
        aggHash = agg.hashCode();
    }

    void start() {
        Thread t = new Thread(new AggConsumer(agg));
        t.start();
    }

    public boolean hasUpdates() {
        return aggHash != agg.hashCode();
    }

    public Aggregations getAggregations() {
        return agg;
    }

    public static void main(String[] args) throws Exception {
        DataConsumption dc = new DataConsumption();
        dc.start();
        TimerUtil.block(TimeUnit.SECONDS, 300);
    }
}
