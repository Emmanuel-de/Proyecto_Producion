package com.factorysim.model;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class Vehicle implements Runnable {
    private final String id;
    private final VehicleType type;
    private final Warehouse warehouse;
    private final Stats stats;
    private final AtomicBoolean running;
    private final AtomicBoolean paused;
    private final List<ProductItem> inTransit;

    private volatile String status = "Detenido";
    private volatile ProductItem carrying = null;

    public Vehicle(String id, VehicleType type, Warehouse warehouse, Stats stats,
                   AtomicBoolean running, AtomicBoolean paused, List<ProductItem> inTransit) {
        this.id = id;
        this.type = type;
        this.warehouse = warehouse;
        this.stats = stats;
        this.running = running;
        this.paused = paused;
        this.inTransit = inTransit;
    }

    public String getId() { return id; }
    public String getStatus() { return status; }
    public VehicleType getType() { return type; }
    public ProductItem getCarrying() { return carrying; }

    @Override
    public void run() {
        status = "Esperando";
        while (running.get()) {
            if (paused.get()) { status = "Pausado"; sleep(100); continue; }
            try {
                status = "Tomando pedido";
                ProductItem item = warehouse.take();
                carrying = item;
                inTransit.add(item);
                stats.incVehiclesActive();
                status = "En ruta con " + item;

                int travelMs = ThreadLocalRandom.current().nextInt(type.minMs, type.maxMs + 1);
                sleep(travelMs);

                // Entregado
                stats.incDelivered(item.getType());
                inTransit.remove(item);
                carrying = null;
                stats.decVehiclesActive();
                status = "Entregado";

                // Pequeña espera antes de volver
                sleep(100 + new Random().nextInt(200));
            } catch (InterruptedException e) {
                status = "Interrumpido";
                Thread.currentThread().interrupt();
                break;
            }
        }
        status = "Detenido";
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
    }
}