package com.proyecto1.businessunit;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BusinessUnitGUI extends JFrame {

    private final HistoricalEventReader eventReader = new HistoricalEventReader();
    private JComboBox<String> cityComboBox;
    private JButton searchEventsButton;
    private JTextArea eventsTextArea;
    private JComboBox<String> originComboBox;
    private JComboBox<String> destinationComboBox;
    private JButton searchFlightsButton;
    private JTextArea flightsTextArea;
    public BusinessUnitGUI() {
        setTitle("Business Unit - Eventos y Vuelos");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Eventos", createEventsPanel());
        tabbedPane.addTab("Vuelos", createFlightsPanel());
        add(tabbedPane);
    }
    private JPanel createEventsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Ciudad:"));
        cityComboBox = new JComboBox<>(new String[]{"Barcelona", "Madrid", "Valencia", "London",
                "Berlin", "Rome", "Amsterdam", "Lisbon", "Tokyo", "Sydney", "Dubai"});
        topPanel.add(cityComboBox);
        searchEventsButton = new JButton("Buscar eventos");
        topPanel.add(searchEventsButton);
        panel.add(topPanel, BorderLayout.NORTH);
        eventsTextArea = new JTextArea();
        eventsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(eventsTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        searchEventsButton.addActionListener(e -> searchEvents());
        return panel;
    }
    private JPanel createFlightsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Origen:"));
        originComboBox = new JComboBox<>(new String[]{"MAD", "BCN", "LHR", "JFK", "CDG", "FRA", "AMS", "NRT", "SYD", "DXB"});
        topPanel.add(originComboBox);
        topPanel.add(new JLabel("Destino:"));
        destinationComboBox = new JComboBox<>(new String[]{"MAD", "BCN", "LHR", "JFK", "CDG", "FRA", "AMS", "NRT", "SYD", "DXB"});
        topPanel.add(destinationComboBox);
        searchFlightsButton = new JButton("Buscar vuelos");
        topPanel.add(searchFlightsButton);
        panel.add(topPanel, BorderLayout.NORTH);
        flightsTextArea = new JTextArea();
        flightsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(flightsTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        searchFlightsButton.addActionListener(e -> searchFlights());
        return panel;
    }
    private void searchEvents() {
        String city = (String) cityComboBox.getSelectedItem();
        if (city == null || city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una ciudad.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<String> eventos = eventReader.leerEventos(city);
        eventsTextArea.setText("");
        if (eventos.isEmpty()) {
            eventsTextArea.setText("No se encontraron eventos para la ciudad seleccionada.");
        } else {
            for (String evento : eventos) {
                String[] parts = evento.split("\\|");
                String artista = "";
                String nombreEvento = "";
                String tipo = "";
                String ciudad = "";
                String fecha = "";

                if (parts.length >= 4) {
                    String[] artistaNombre = parts[0].split(" - ", 2);
                    artista = artistaNombre.length > 0 ? artistaNombre[0].trim() : "";
                    nombreEvento = artistaNombre.length > 1 ? artistaNombre[1].trim() : "";
                    tipo = parts[1].trim();
                    ciudad = parts[2].trim();
                    fecha = parts[3].trim();
                } else if (parts.length == 3) {
                    String[] artistaNombre = parts[0].split(" - ", 2);
                    artista = artistaNombre.length > 0 ? artistaNombre[0].trim() : "";
                    nombreEvento = artistaNombre.length > 1 ? artistaNombre[1].trim() : "";
                    tipo = "";  // No hay tipo
                    ciudad = parts[1].trim();
                    fecha = parts[2].trim();
                } else {
                    String[] artistaNombre = parts[0].split(" - ", 2);
                    artista = artistaNombre.length > 0 ? artistaNombre[0].trim() : "";
                    nombreEvento = artistaNombre.length > 1 ? artistaNombre[1].trim() : "";
                }

                if (nombreEvento.isEmpty()) {
                    nombreEvento = "(sin nombre)";
                }
                if (tipo.isEmpty()) {
                    tipo = "(sin tipo)";
                }
                String textoFormateado = String.format(
                        "Evento: %s | Nombre: %s | Tipo: %s | Ciudad: %s | Fecha: %s",
                        artista,
                        nombreEvento,
                        tipo,
                        ciudad,
                        fecha
                );
                eventsTextArea.append(textoFormateado + "\n");
            }
        }
    }
    private void searchFlights() {
        String origin = (String) originComboBox.getSelectedItem();
        String destination = (String) destinationComboBox.getSelectedItem();
        if (origin == null || origin.isEmpty() || destination == null || destination.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione origen y destino.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (origin.equals(destination)) {
            JOptionPane.showMessageDialog(this, "El origen y destino no pueden ser iguales.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<String> vuelos = eventReader.leerVuelos(origin, destination);
        flightsTextArea.setText("");
        if (vuelos.isEmpty()) {
            flightsTextArea.setText("No se encontraron vuelos para el origen y destino seleccionados.");
        } else {
            vuelos.forEach(flight -> flightsTextArea.append(flight + "\n"));
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BusinessUnitGUI gui = new BusinessUnitGUI();
            gui.setVisible(true);
        });
    }
}