package org.datafeed.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@lombok.EqualsAndHashCode
public class Aggregations {
    List<CustomerType> customerTypes;
    List<Location> locations;
    List<Meter> meters;

    public Aggregations() {
        customerTypes = Collections.synchronizedList(new ArrayList<>());
        locations = Collections.synchronizedList(new ArrayList<>());
        meters = Collections.synchronizedList(new ArrayList<>());
    }
}
