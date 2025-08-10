package com.factorysim;
import com.factorysim.model.*;
import com.factorysim.ui.*;

import javax.swing.*;


public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Config básica Look&Feel
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

            // Configuración del simulador
            int queueCapacity = 100;                 // capacidad del almacén
            int machineCount = 3;                    // mínimo 3
            int vehicleCount = 3;                    // mínimo 3

            Warehouse warehouse = new Warehouse(queueCapacity);
            Stats stats = new Stats();
            SimulatorController controller = new SimulatorController(warehouse, stats, machineCount, vehicleCount);

            // Ventanas (3 ventanas separadas)
            ProductionWindow productionWindow = new ProductionWindow(controller);
            productionWindow.setLocation(60, 60);
            productionWindow.setVisible(true);

            LogisticsWindow logisticsWindow = new LogisticsWindow(controller);
            logisticsWindow.setLocation(680, 60);
            logisticsWindow.setVisible(true);

            ReportsWindow reportsWindow = new ReportsWindow(controller);
            reportsWindow.setLocation(380, 420);
            reportsWindow.setVisible(true);
        });
    }
}
