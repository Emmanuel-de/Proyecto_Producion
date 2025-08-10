package com.factorysim.model;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Machine implements Runnable {
    private final String id;
    private final Warehouse warehouse;
    private final Stats stats;
    private final AtomicBoolean running;
    private final AtomicBoolean paused;

    private volatile String status = "Detenida";
    private volatile ProductItem lastProduced = null;
    private final Random rnd = new Random();

    public Machine(String id, Warehouse warehouse, Stats stats, AtomicBoolean running, AtomicBoolean paused) {
        this.id = id;
        this.warehouse = warehouse;
        this.stats = stats;
        this.running = running;
        this.paused = paused;
    }

    public String getId() { return id; }
    public String getStatus() { return status; }
    public ProductItem getLastProduced() { return lastProduced; }

    @Override
    public void run() {
        status = "Iniciando";
        while (running.get()) {
            if (paused.get()) { status = "Pausada"; sleep(100); continue; }
            try {
                status = "Produciendo";
                ProductType type = ProductType.values()[rnd.nextInt(ProductType.values().length)];
                // tiempo de producción simulado (300–900 ms)
                sleep(300 + rnd.nextInt(600));
                ProductItem item = new ProductItem(type);
                warehouse.put(item);
                lastProduced = item;
                stats.incProduced(type);
                status = "Listo: " + item;
                // pequeño descanso entre ciclos
                sleep(80 + rnd.nextInt(120));
            } catch (InterruptedException e) {
                status = "Interrumpida";
                Thread.currentThread().interrupt();
                break;
            }
        }
        status = "Detenida";
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
    }
}
