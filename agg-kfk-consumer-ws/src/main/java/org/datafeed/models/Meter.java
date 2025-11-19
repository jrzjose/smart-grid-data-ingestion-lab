package org.datafeed.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Meter extends Consumption {
    private String meterId;

    public Meter(Consumption consumption, String meterId) {
        super(consumption.getEnergyConsumption(), consumption.getStartTime(), consumption.getEndTime());
        this.meterId = meterId;
    }
}
