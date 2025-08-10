package com.factorysim;
import com.factorysim.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimulatorController {
    private final Warehouse warehouse;
    private final Stats stats;
    private final List<Machine> machines;
    private final List<Vehicle> vehicles;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean paused = new AtomicBoolean(false);

    // Lista de productos actualmente en tránsito (para UI)
    private final List<ProductItem> inTransit = new CopyOnWriteArrayList<>();

    public SimulatorController(Warehouse warehouse, Stats stats, int machineCount, int vehicleCount) {
        this.warehouse = warehouse;
        this.stats = stats;
        this.machines = new ArrayList<>();
        this.vehicles = new ArrayList<>();

        for (int i = 1; i <= machineCount; i++) {
            machines.add(new Machine("M-" + i, warehouse, stats, running, paused));
        }
        for (int i = 1; i <= vehicleCount; i++) {
            VehicleType type = (i % 2 == 0) ? VehicleType.DRONE : VehicleType.TRUCK;
            vehicles.add(new Vehicle("V-" + i, type, warehouse, stats, running, paused, inTransit));
        }
    }

    public void start() {
        if (running.get()) return;
        running.set(true);
        paused.set(false);
        stats.resetDeliveredActiveVehicles();
        for (Machine m : machines) new Thread(m, m.getId()).start();
        for (Vehicle v : vehicles) new Thread(v, v.getId()).start();
    }

    public void pause() { paused.set(true); }
    public void resume() { paused.set(false); }

    public void stopAndReset() {
        running.set(false);
        paused.set(false);
        // vaciar almacén y tránsito
        warehouse.clear();
        inTransit.clear();
        stats.resetAll();
        // dejar que los hilos terminen por sí solos (observan running)
    }

    public boolean isRunning() { return running.get(); }
    public boolean isPaused() { return paused.get(); }

    public List<Machine> getMachines() { return Collections.unmodifiableList(machines); }
    public List<Vehicle> getVehicles() { return Collections.unmodifiableList(vehicles); }
    public Warehouse getWarehouse() { return warehouse; }
    public Stats getStats() { return stats; }
    public List<ProductItem> getInTransit() { return Collections.unmodifiableList(inTransit); }
}
