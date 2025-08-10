package com.factorysim.model;

public enum VehicleType {
    TRUCK("Camión", 800, 1400),
    DRONE("Drone", 400, 900);

    public final String label;
    public final int minMs;
    public final int maxMs;

    VehicleType(String label, int minMs, int maxMs) {
        this.label = label;
        this.minMs = minMs;
        this.maxMs = maxMs;
    }
}