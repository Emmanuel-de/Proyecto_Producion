package com.factorysim.ui;

import com.factorysim.SimulatorController;
import com.factorysim.model.ProductItem;
import com.factorysim.model.Vehicle;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class LogisticsWindow extends JFrame {
    private final SimulatorController controller;
    private final JTable vehicleTable;
    private final DefaultListModel<String> transitModel = new DefaultListModel<>();

    public LogisticsWindow(SimulatorController controller) {
        super("Panel de Logística");
        this.controller = controller;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(580, 360);

        setLayout(new BorderLayout());
        vehicleTable = new JTable(new VehicleTableModel(controller.getVehicles()));
        add(new JScrollPane(vehicleTable), BorderLayout.CENTER);

        JList<String> transitList = new JList<>(transitModel);
        JPanel right = new JPanel(new BorderLayout());
        right.add(new JLabel("Productos en tránsito"), BorderLayout.NORTH);
        right.add(new JScrollPane(transitList), BorderLayout.CENTER);
        right.setPreferredSize(new Dimension(220, 100));
        add(right, BorderLayout.EAST);

        new Timer(300, e -> refresh()).start();
    }

    private void refresh() {
        ((AbstractTableModel) vehicleTable.getModel()).fireTableDataChanged();
        transitModel.clear();
        for (ProductItem p : controller.getInTransit()) transitModel.addElement(p.toString());
    }

    static class VehicleTableModel extends AbstractTableModel {
        private final String[] cols = {"Vehículo", "Tipo", "Estado", "Transportando"};
        private final List<Vehicle> vehicles;
        VehicleTableModel(List<Vehicle> vehicles) { this.vehicles = vehicles; }
        @Override public int getRowCount() { return vehicles.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }
        @Override public Object getValueAt(int r, int c) {
            Vehicle v = vehicles.get(r);
            return switch (c) {
                case 0 -> v.getId();
                case 1 -> v.getType().label;
                case 2 -> v.getStatus();
                case 3 -> v.getCarrying() == null ? "—" : v.getCarrying().toString();
                default -> "";
            };
        }
        @Override public boolean isCellEditable(int r, int c) { return false; }
    }
}