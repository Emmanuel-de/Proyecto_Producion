package com.factorysim.model;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Stats {
    private final Map<ProductType, AtomicInteger> produced = new EnumMap<>(ProductType.class);
    private final Map<ProductType, AtomicInteger> delivered = new EnumMap<>(ProductType.class);
    private final AtomicInteger vehiclesActive = new AtomicInteger(0);

    public Stats() {
        for (ProductType t : ProductType.values()) {
            produced.put(t, new AtomicInteger(0));
            delivered.put(t, new AtomicInteger(0));
        }
    }

    public void incProduced(ProductType t) { produced.get(t).incrementAndGet(); }
    public void incDelivered(ProductType t) { delivered.get(t).incrementAndGet(); }

    public int getProduced(ProductType t) { return produced.get(t).get(); }
    public int getDelivered(ProductType t) { return delivered.get(t).get(); }

    public int getVehiclesActive() { return vehiclesActive.get(); }
    public void incVehiclesActive() { vehiclesActive.incrementAndGet(); }
    public void decVehiclesActive() { vehiclesActive.decrementAndGet(); }

    public int getProducedTotal() { return produced.values().stream().mapToInt(AtomicInteger::get).sum(); }
    public int getDeliveredTotal() { return delivered.values().stream().mapToInt(AtomicInteger::get).sum(); }

    public void resetDeliveredActiveVehicles() {
        for (ProductType t : ProductType.values()) delivered.get(t).set(0);
        vehiclesActive.set(0);
    }

    public void resetAll() {
        for (ProductType t : ProductType.values()) {
            produced.get(t).set(0);
            delivered.get(t).set(0);
        }
        vehiclesActive.set(0);
    }
}
