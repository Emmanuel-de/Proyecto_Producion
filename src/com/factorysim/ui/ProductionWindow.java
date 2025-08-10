package com.factorysim.ui;

import com.factorysim.SimulatorController;
import com.factorysim.model.Machine;
import com.factorysim.model.ProductItem;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class ProductionWindow extends JFrame {
    private final SimulatorController controller;
    private final JTable table;

    public ProductionWindow(SimulatorController controller) {
        super("Panel de Producción");
        this.controller = controller;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(580, 300);

        table = new JTable(new MachineTableModel(controller.getMachines()));
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Timer de refresco UI
        new Timer(300, e -> ((AbstractTableModel) table.getModel()).fireTableDataChanged()).start();
    }

    static class MachineTableModel extends AbstractTableModel {
        private final String[] cols = {"Máquina", "Estado", "Último producto"};
        private final List<Machine> machines;
        MachineTableModel(List<Machine> machines) { this.machines = machines; }
        @Override public int getRowCount() { return machines.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }
        @Override public Object getValueAt(int r, int c) {
            Machine m = machines.get(r);
            return switch (c) {
                case 0 -> m.getId();
                case 1 -> m.getStatus();
                case 2 -> {
                    ProductItem p = m.getLastProduced();
                    yield (p == null) ? "—" : p.toString();
                }
                default -> "";
            };
        }
        @Override public boolean isCellEditable(int r, int c) { return false; }
    }
}