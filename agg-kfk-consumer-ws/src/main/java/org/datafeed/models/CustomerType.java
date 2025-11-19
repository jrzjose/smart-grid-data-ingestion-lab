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
public class CustomerType extends Consumption {
    private String customerType;

    public CustomerType(Consumption consumption, String customerType) {
        super(consumption.getEnergyConsumption(), consumption.getStartTime(), consumption.getEndTime());
        this.customerType = customerType;
    }
}
