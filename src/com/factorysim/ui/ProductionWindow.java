package com.factorysim.ui;

import com.factorysim.SimulatorController;
import com.factorysim.model.Machine;
import com.factorysim.model.ProductItem;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class ProductionWindow extends JFrame {
    private final SimulatorController controller;
    private final JTable table;

    public ProductionWindow(SimulatorController controller) {
        super("🏭 Panel de Producción");
        this.controller = controller;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(680, 350);

        // Configurar fondo moderno
        getContentPane().setBackground(new Color(240, 242, 247));
        
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(new Color(240, 242, 247));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(contentPanel);

        // Título elegante
        JPanel headerPanel = createHeaderPanel();
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Tabla mejorada
        table = new JTable(new MachineTableModel(controller.getMachines()));
        setupTable();
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de información adicional
        JPanel infoPanel = createInfoPanel();
        contentPanel.add(infoPanel, BorderLayout.SOUTH);

        // Timer de refresco UI
        new Timer(300, e -> {
            ((AbstractTableModel) table.getModel()).fireTableDataChanged();
            updateInfoPanel(infoPanel);
        }).start();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel titleLabel = new JLabel("🏭 Línea de Producción - Monitoreo en Tiempo Real");
        // Fuente para emojis
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        titleLabel.setForeground(new Color(33, 37, 41));
        
        JLabel subtitleLabel = new JLabel("Estado actual de todas las máquinas de producción");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        subtitleLabel.setForeground(new Color(108, 117, 125));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitleLabel);

        header.add(titlePanel, BorderLayout.WEST);
        
        // Indicador de estado general
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setOpaque(false);
        
        JLabel statusIndicator = new JLabel("🟢 Sistema Activo");
        // Fuente para emojis
        statusIndicator.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        statusIndicator.setForeground(new Color(40, 167, 69));
        statusPanel.add(statusIndicator);
        
        header.add(statusPanel, BorderLayout.EAST);
        
        return header;
    }

    private void setupTable() {
    // Aumentar la altura de la fila para dar espacio a los emojis
    table.setRowHeight(45); 
    
    table.setFont(new Font("Arial", Font.PLAIN, 12));
    table.setGridColor(new Color(222, 226, 230));
    table.setSelectionBackground(new Color(232, 245, 255));
    table.setSelectionForeground(new Color(33, 37, 41));
    table.setShowGrid(true);
    table.setIntercellSpacing(new Dimension(1, 1));
    
    // Configurar el encabezado
    table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
    table.getTableHeader().setBackground(new Color(52, 58, 64));
    table.getTableHeader().setForeground(Color.WHITE);
    // Aumentar la altura del encabezado
    table.getTableHeader().setPreferredSize(new Dimension(0, 45)); 
    
    // Renderer personalizado para las celdas
    table.setDefaultRenderer(Object.class, new MachineTableCellRenderer());
    
    // Configurar anchos de columnas
    table.getColumnModel().getColumn(0).setPreferredWidth(100);
    table.getColumnModel().getColumn(1).setPreferredWidth(200);
    table.getColumnModel().getColumn(2).setPreferredWidth(150);
}

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Indicadores de rendimiento
        JPanel activePanel = createMetricPanel("⚡", "Máquinas Activas", "0", new Color(40, 167, 69));
        JPanel pausedPanel = createMetricPanel("⏸️", "En Pausa", "0", new Color(255, 193, 7));
        JPanel stoppedPanel = createMetricPanel("⏹️", "Detenidas", "0", new Color(220, 53, 69));

        infoPanel.add(activePanel);
        infoPanel.add(pausedPanel);
        infoPanel.add(stoppedPanel);

        return infoPanel;
    }

    private JPanel createMetricPanel(String icon, String title, String value, Color accentColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        // Fuente para emojis
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 11));
        titleLabel.setForeground(new Color(108, 117, 125));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        valueLabel.setForeground(accentColor);
        valueLabel.setName(title); // Para identificar en updateInfoPanel

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.add(iconLabel);
        content.add(Box.createVerticalStrut(5));
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(5));
        content.add(valueLabel);

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private void updateInfoPanel(JPanel infoPanel) {
        List<Machine> machines = controller.getMachines();
        int active = 0, paused = 0, stopped = 0;

        for (Machine machine : machines) {
            String status = machine.getStatus();
            if (status.contains("Produciendo") || status.contains("Listo")) {
                active++;
            } else if (status.contains("Pausada")) {
                paused++;
            } else if (status.contains("Detenida")) {
                stopped++;
            }
        }

        // Actualizar los valores en los paneles
        updateMetricValue(infoPanel, "Máquinas Activas", String.valueOf(active));
        updateMetricValue(infoPanel, "En Pausa", String.valueOf(paused));
        updateMetricValue(infoPanel, "Detenidas", String.valueOf(stopped));
    }

    private void updateMetricValue(Container parent, String metricName, String newValue) {
        for (Component comp : parent.getComponents()) {
            if (comp instanceof JPanel) {
                updateMetricValue((Container) comp, metricName, newValue);
            } else if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (metricName.equals(label.getName())) {
                    label.setText(newValue);
                    return;
                }
            }
        }
    }

    // Renderer personalizado para la tabla
    static class MachineTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // Se usa la fuente de emojis para asegurar la compatibilidad
            setFont(new Font("Segoe UI Emoji", getFont().getStyle(), getFont().getSize()));

            if (!isSelected) {
                // Color de fondo alternado
                if (row % 2 == 0) {
                    setBackground(new Color(248, 249, 250));
                } else {
                    setBackground(Color.WHITE);
                }
            }

            // Estilo según la columna
            if (column == 0) { // Máquina ID
                setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
                setForeground(new Color(33, 37, 41));
                setText("🔧 " + value);
            } else if (column == 1) { // Estado
                setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
                String status = value.toString();
                
                if (status.contains("Produciendo")) {
                    setForeground(new Color(40, 167, 69));
                    setText("🟢 " + status);
                } else if (status.contains("Pausada")) {
                    setForeground(new Color(255, 193, 7));
                    setText("🟡 " + status);
                } else if (status.contains("Detenida")) {
                    setForeground(new Color(220, 53, 69));
                    setText("🔴 " + status);
                } else if (status.contains("Listo")) {
                    setForeground(new Color(23, 162, 184));
                    setText("✅ " + status);
                } else {
                    setForeground(new Color(108, 117, 125));
                    setText("⚪ " + status);
                }
            } else if (column == 2) { // Último producto
                setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
                if (value.toString().equals("—")) {
                    setForeground(new Color(108, 117, 125));
                    setText("📦 Sin producción");
                } else {
                    setForeground(new Color(33, 37, 41));
                    String product = value.toString();
                    String emoji = "📦";
                    if (product.contains("A#")) emoji = "🟨";
                    else if (product.contains("B#")) emoji = "🟦";
                    else if (product.contains("C#")) emoji = "🟪";
                    setText(emoji + " " + product);
                }
            }

            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
            return this;
        }
    }

    // Modelo de tabla (sin cambios en la lógica)
    static class MachineTableModel extends AbstractTableModel {
        private final String[] cols = {"Máquina", "Estado", "Último producto"};
        private final List<Machine> machines;
        
        MachineTableModel(List<Machine> machines) { 
            this.machines = machines; 
        }
        
        @Override public int getRowCount() { 
            return machines.size(); 
        }
        
        @Override public int getColumnCount() { 
            return cols.length; 
        }
        
        @Override public String getColumnName(int c) { 
            return cols[c]; 
        }
        
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
        
        @Override public boolean isCellEditable(int r, int c) { 
            return false; 
        }
    }
}