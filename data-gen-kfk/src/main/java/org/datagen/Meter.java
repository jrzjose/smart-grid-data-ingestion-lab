package org.datagen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Meter {
    private int meterId;
    private long intervalTime;
    private int intervalLength;
    private double consumptionValue;
    private String readingSource;
    private String locationId;
    private String customerType; //r=residential, c=commercial, i=industrial

    public String toJson() {
        return "{meter_id:" + meterId + ", interval_time:" + intervalTime + ", interval_length:"
                + intervalLength + ", consumption_value:" + consumptionValue + ", reading_source:'" + readingSource
                + "', customerType:'" + customerType
                + "', location_id:'" + locationId + "'}";
    }
}
