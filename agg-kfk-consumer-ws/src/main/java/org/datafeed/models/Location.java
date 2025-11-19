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
public class Location extends Consumption {
    private String locationId;

    public Location(Consumption consumption, String locationId) {
        super(consumption.getEnergyConsumption(), consumption.getStartTime(), consumption.getEndTime());
        this.locationId = locationId;
    }
}
