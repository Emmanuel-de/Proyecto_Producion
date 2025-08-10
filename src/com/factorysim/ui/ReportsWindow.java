package com.factorysim.ui;

import com.factorysim.SimulatorController;
import com.factorysim.model.ProductType;
import com.factorysim.model.Stats;
import com.factorysim.model.Warehouse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
    
    // Datos para gráficas
    private final List<Integer> productionHistory = new ArrayList<>();
    private final List<Integer> deliveryHistory = new ArrayList<>();
    private final List<Integer> warehouseHistory = new ArrayList<>();
    private final int MAX_HISTORY = 50;
    
    // Paneles para gráficas
    private ChartPanel productionChart;
    private ChartPanel deliveryChart;
    private ChartPanel warehouseChart;

    public ReportsWindow(SimulatorController controller) {
        super("📊 Reportes y Control");
        this.controller = controller;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        
        // Configurar colores modernos
        getContentPane().setBackground(new Color(240, 242, 247));

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(new Color(240, 242, 247));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(content);

        content.add(buildMainPanel(), BorderLayout.CENTER);
        content.add(buildControls(), BorderLayout.SOUTH);

        // Inicializar datos históricos
        for (int i = 0; i < MAX_HISTORY; i++) {
            productionHistory.add(0);
            deliveryHistory.add(0);
            warehouseHistory.add(0);
        }

        new Timer(1000, e -> refresh()).start();
    }

    private JPanel buildMainPanel() {
        JPanel main = new JPanel(new GridLayout(2, 2, 15, 15));
        main.setOpaque(false);
        
        // Panel de estadísticas mejorado
        main.add(buildStatsPanel());
        
        // Gráfica de producción
        productionChart = new ChartPanel("📈 Producción Total", Color.decode("#4CAF50"), productionHistory);
        main.add(productionChart);
        
        // Gráfica de entregas
        deliveryChart = new ChartPanel("🚛 Entregas Totales", Color.decode("#2196F3"), deliveryHistory);
        main.add(deliveryChart);
        
        // Gráfica de almacén
        warehouseChart = new ChartPanel("📦 Ocupación Almacén", Color.decode("#FF9800"), warehouseHistory);
        main.add(warehouseChart);
        
        return main;
    }

    private JPanel buildStatsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Título con estilo
        JLabel title = new JLabel("📊 Estadísticas en Tiempo Real");
        // Aseguramos que el título tenga una fuente con soporte para emojis
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        title.setForeground(new Color(33, 37, 41));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(15));

        // Sección de Producción
        panel.add(createSectionLabel("🏭 Producción", new Color(76, 175, 80)));
        panel.add(createStatRow("Productos A:", producedA, new Color(255, 193, 7)));
        panel.add(createStatRow("Productos B:", producedB, new Color(33, 150, 243)));
        panel.add(createStatRow("Productos C:", producedC, new Color(156, 39, 176)));
        panel.add(Box.createVerticalStrut(10));

        // Sección de Entregas
        panel.add(createSectionLabel("🚚 Entregas", new Color(33, 150, 243)));
        panel.add(createStatRow("Entregados A:", deliveredA, new Color(255, 193, 7)));
        panel.add(createStatRow("Entregados B:", deliveredB, new Color(33, 150, 243)));
        panel.add(createStatRow("Entregados C:", deliveredC, new Color(156, 39, 176)));
        panel.add(Box.createVerticalStrut(10));

        // Vehículos activos
        panel.add(createSectionLabel("🚛 Logística", new Color(255, 152, 0)));
        panel.add(createStatRow("Vehículos activos:", vehiclesActive, new Color(244, 67, 54)));
        panel.add(Box.createVerticalStrut(15));

        // Barra del almacén mejorada
        JPanel warehousePanel = new JPanel(new BorderLayout(5, 5));
        warehousePanel.setOpaque(false);
        JLabel warehouseLabel = new JLabel("📦 Almacén");
        // Se añade la fuente de emojis aquí también
        warehouseLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        warehouseLabel.setForeground(new Color(255, 152, 0));
        warehousePanel.add(warehouseLabel, BorderLayout.NORTH);
        
        warehouseBar.setStringPainted(true);
        warehouseBar.setForeground(Color.WHITE);
        warehouseBar.setBackground(new Color(230, 230, 230));
        warehouseBar.setBorder(BorderFactory.createRaisedBevelBorder());
        warehousePanel.add(warehouseBar, BorderLayout.CENTER);
        panel.add(warehousePanel);

        return panel;
    }
    
    // Método para etiquetas de sección, se le añadió la fuente
    private JLabel createSectionLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createStatRow(String label, JLabel value, Color accentColor) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Arial", Font.PLAIN, 11));
        labelComp.setForeground(new Color(108, 117, 125));
        
        value.setFont(new Font("Arial", Font.BOLD, 12));
        value.setForeground(accentColor);
        value.setHorizontalAlignment(SwingConstants.RIGHT);
        
        row.add(labelComp, BorderLayout.WEST);
        row.add(value, BorderLayout.EAST);
        return row;
    }

    private JPanel buildControls() {
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        controls.setBackground(new Color(248, 249, 250));
        controls.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Aseguramos que los botones también usen la fuente de emojis
        JButton start = createStyledButton("▶️ Iniciar", new Color(40, 167, 69));
         start.setForeground(Color.BLACK); // Establece el color de la letra en negro
        JButton pause = createStyledButton("⏸️ Pausar", new Color(255, 193, 7));
         pause.setForeground(Color.BLACK); // Establece el color de la letra en negro
        JButton resume = createStyledButton("▶️ Reanudar", new Color(23, 162, 184));
         resume.setForeground(Color.BLACK); // Establece el color de la letra en negro
        JButton reset = createStyledButton("🔄 Reiniciar", new Color(220, 53, 69));
         reset.setForeground(Color.BLACK); // Establece el color de la letra en negro

        start.addActionListener(e -> controller.start());
        pause.addActionListener(e -> controller.pause());
        resume.addActionListener(e -> controller.resume());
        reset.addActionListener(e -> {
            controller.stopAndReset();
            clearHistory();
        });

        controls.add(start);
        controls.add(pause);
        controls.add(resume);
        controls.add(reset);
        return controls;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        // Se añade la fuente de emojis aquí
        button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void clearHistory() {
        productionHistory.clear();
        deliveryHistory.clear();
        warehouseHistory.clear();
        for (int i = 0; i < MAX_HISTORY; i++) {
            productionHistory.add(0);
            deliveryHistory.add(0);
            warehouseHistory.add(0);
        }
    }

    private void refresh() {
        Stats s = controller.getStats();
        
        // Actualizar labels
        producedA.setText(String.valueOf(s.getProduced(ProductType.A)));
        producedB.setText(String.valueOf(s.getProduced(ProductType.B)));
        producedC.setText(String.valueOf(s.getProduced(ProductType.C)));

        deliveredA.setText(String.valueOf(s.getDelivered(ProductType.A)));
        deliveredB.setText(String.valueOf(s.getDelivered(ProductType.B)));
        deliveredC.setText(String.valueOf(s.getDelivered(ProductType.C)));

        vehiclesActive.setText(String.valueOf(s.getVehiclesActive()));

        // Actualizar barra del almacén
        Warehouse w = controller.getWarehouse();
        int capKnown = w.capacity();
        int size = w.size();
        if (capKnown > 0) {
            int usedPercent = (int) Math.round((size * 100.0) / capKnown);
            warehouseBar.setMaximum(100);
            warehouseBar.setValue(usedPercent);
            warehouseBar.setString(usedPercent + "% (" + size + "/" + capKnown + ")");
            
            // Color dinámico de la barra
            if (usedPercent > 80) {
                warehouseBar.setForeground(Color.decode("#dc3545"));
            } else if (usedPercent > 60) {
                warehouseBar.setForeground(Color.decode("#ffc107"));
            } else {
                warehouseBar.setForeground(Color.decode("#28a745"));
            }
        } else {
            warehouseBar.setIndeterminate(true);
        }

        // Actualizar datos históricos para gráficas
        updateHistory(productionHistory, s.getProducedTotal());
        updateHistory(deliveryHistory, s.getDeliveredTotal());
        updateHistory(warehouseHistory, size);

        // Repintar gráficas
        productionChart.repaint();
        deliveryChart.repaint();
        warehouseChart.repaint();
    }

    private void updateHistory(List<Integer> history, int newValue) {
        if (history.size() >= MAX_HISTORY) {
            history.remove(0);
        }
        history.add(newValue);
    }

    // Clase interna para panel de gráficas
    private static class ChartPanel extends JPanel {
        private final String title;
        private final Color lineColor;
        private final List<Integer> data;

        public ChartPanel(String title, Color lineColor, List<Integer> data) {
            this.title = title;
            this.lineColor = lineColor;
            this.data = data;
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth() - 40;
            int height = getHeight() - 60;
            int startX = 20;
            int startY = 30;

            // Título
            // Se le añadió la fuente de emojis aquí
            g2.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
            g2.setColor(new Color(33, 37, 41));
            FontMetrics fm = g2.getFontMetrics();
            int titleX = (getWidth() - fm.stringWidth(title)) / 2;
            g2.drawString(title, titleX, 20);

            if (data.isEmpty()) return;

            // Encontrar min y max
            int maxVal = data.stream().mapToInt(Integer::intValue).max().orElse(1);
            maxVal = Math.max(maxVal, 1); // Evitar división por cero

            // Dibujar ejes
            g2.setColor(new Color(200, 200, 200));
            g2.drawLine(startX, startY, startX, startY + height);
            g2.drawLine(startX, startY + height, startX + width, startY + height);

            // Dibujar líneas de cuadrícula
            g2.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{2}, 0));
            for (int i = 1; i <= 5; i++) {
                int y = startY + (height * i / 5);
                g2.drawLine(startX, y, startX + width, y);
            }

            // Dibujar datos
            g2.setStroke(new BasicStroke(2));
            g2.setColor(lineColor);
            
            for (int i = 1; i < data.size(); i++) {
                int x1 = startX + (width * (i - 1) / (data.size() - 1));
                int y1 = startY + height - (int) ((double) data.get(i - 1) / maxVal * height);
                int x2 = startX + (width * i / (data.size() - 1));
                int y2 = startY + height - (int) ((double) data.get(i) / maxVal * height);
                g2.drawLine(x1, y1, x2, y2);
            }

            // Dibujar puntos
            g2.setColor(lineColor.darker());
            for (int i = 0; i < data.size(); i += 5) { // Mostrar cada 5 puntos
                int x = startX + (width * i / (data.size() - 1));
                int y = startY + height - (int) ((double) data.get(i) / maxVal * height);
                g2.fillOval(x - 2, y - 2, 4, 4);
            }

            // Valor actual
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.setColor(lineColor);
            String currentValue = String.valueOf(data.get(data.size() - 1));
            int valueX = getWidth() - 20 - fm.stringWidth(currentValue);
            g2.drawString(currentValue, valueX, startY + 15);

            g2.dispose();
        }
    }
}