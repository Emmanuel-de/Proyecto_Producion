package com.factorysim.ui;

import com.factorysim.SimulatorController;
import com.factorysim.model.ProductType;
import com.factorysim.model.Stats;
import com.factorysim.model.Warehouse;

import javax.swing.*;
import java.awt.*;

public class ReportsWindow extends JFrame {
    private final SimulatorController controller;

    private final JLabel producedA = new JLabel("0");
    private final JLabel producedB = new JLabel("0");
    private final JLabel producedC = new JLabel("0");
    private final JLabel deliveredA = new JLabel("0");
    private final JLabel deliveredB = new JLabel("0");
    private final JLabel deliveredC = new JLabel("0");
    private final JLabel vehiclesActive = new JLabel("0");

    private final JProgressBar warehouseBar = new JProgressBar(0, 100);

    public ReportsWindow(SimulatorController controller) {
        super("Reportes y Control");
        this.controller = controller;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(580, 300);

        JPanel content = new JPanel(new BorderLayout(10,10));
        setContentPane(content);

        content.add(buildStatsPanel(), BorderLayout.CENTER);
        content.add(buildControls(), BorderLayout.SOUTH);

        new Timer(300, e -> refresh()).start();
    }

    private JPanel buildStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Estadísticas"));

        panel.add(new JLabel("Producidos A")); panel.add(producedA);
        panel.add(new JLabel("Producidos B")); panel.add(producedB);
        panel.add(new JLabel("Producidos C")); panel.add(producedC);

        panel.add(new JLabel("Entregados A")); panel.add(deliveredA);
        panel.add(new JLabel("Entregados B")); panel.add(deliveredB);
        panel.add(new JLabel("Entregados C")); panel.add(deliveredC);

        panel.add(new JLabel("Vehículos activos")); panel.add(vehiclesActive);

        JPanel wPanel = new JPanel(new BorderLayout());
        wPanel.setBorder(BorderFactory.createTitledBorder("Almacén (ocupación %)"));
        warehouseBar.setStringPainted(true);
        wPanel.add(warehouseBar, BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new BorderLayout(10,10));
        wrapper.add(panel, BorderLayout.CENTER);
        wrapper.add(wPanel, BorderLayout.SOUTH);
        return wrapper;
    }

    private JPanel buildControls() {
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton start = new JButton("Iniciar");
        JButton pause = new JButton("Pausar");
        JButton resume = new JButton("Reanudar");
        JButton reset = new JButton("Reiniciar");

        start.addActionListener(e -> controller.start());
        pause.addActionListener(e -> controller.pause());
        resume.addActionListener(e -> controller.resume());
        reset.addActionListener(e -> controller.stopAndReset());

        controls.add(start);
        controls.add(pause);
        controls.add(resume);
        controls.add(reset);
        return controls;
    }

    private void refresh() {
        Stats s = controller.getStats();
        producedA.setText(String.valueOf(s.getProduced(ProductType.A)));
        producedB.setText(String.valueOf(s.getProduced(ProductType.B)));
        producedC.setText(String.valueOf(s.getProduced(ProductType.C)));

        deliveredA.setText(String.valueOf(s.getDelivered(ProductType.A)));
        deliveredB.setText(String.valueOf(s.getDelivered(ProductType.B)));
        deliveredC.setText(String.valueOf(s.getDelivered(ProductType.C)));

        vehiclesActive.setText(String.valueOf(s.getVehiclesActive()));

        Warehouse w = controller.getWarehouse();
        int capKnown = w.capacity();
        int size = w.size();
        if (capKnown > 0) {
            int usedPercent = (int) Math.round((size * 100.0) / capKnown);
            warehouseBar.setMaximum(100);
            warehouseBar.setValue(usedPercent);
            warehouseBar.setString(usedPercent + "% (" + size + "/" + capKnown + ")");
        } else {
            warehouseBar.setIndeterminate(true);
        }
    }
}