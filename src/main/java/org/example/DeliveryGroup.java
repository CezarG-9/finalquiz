package org.example;

import java.time.LocalDate;
import java.util.List;

public class DeliveryGroup {
    private String location;
    private LocalDate deliveryData;
    private List<Package> packages;

    public DeliveryGroup(String location, LocalDate deliveryData, List<Package> packages) {
        this.location = location;
        this.deliveryData = deliveryData;
        this.packages = packages;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getDeliveryData() {
        return deliveryData;
    }

    public List<Package> getPackages() {
        return packages;
    }
}
