package com.proyecto1.businessunit;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class BusinessUnitGUI extends JFrame {
    private final DatamartManager datamartManager;
    private JComboBox<String> cityComboBox;
    private JButton searchEventsButton;
    private JTextArea eventsTextArea;
    private JComboBox<String> originComboBox;
    private JComboBox<String> destinationComboBox;
    private JButton searchFlightsButton;
    private JTextArea flightsTextArea;
    public BusinessUnitGUI(DatamartManager datamartManager) {
        this.datamartManager = datamartManager;
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
        eventsTextArea.setText("");
        String ciudadSeleccionada = (String) cityComboBox.getSelectedItem();
        try {
            List<String> eventos = datamartManager.getAllSocialEvents();
            boolean hayResultados = false;
            for (String evento : eventos) {
                if (evento.toLowerCase().contains(ciudadSeleccionada.toLowerCase())) {
                    hayResultados = true;
                    String[] parts = evento.split("\\|");
                    String artista = "", nombreEvento = "", tipo = "", ciudad = "", fecha = "";

                    if (parts.length >= 4) {
                        String[] artistaNombre = parts[0].split(" - ", 2);
                        artista = artistaNombre.length > 0 ? artistaNombre[0].trim() : "";
                        nombreEvento = artistaNombre.length > 1 ? artistaNombre[1].trim() : "";
                        tipo = parts[1].trim();
                        ciudad = parts[2].trim();
                        fecha = parts[3].trim();
                    }

                    if (nombreEvento.isEmpty()) nombreEvento = "(sin nombre)";
                    if (tipo.isEmpty()) tipo = "(sin tipo)";

                    String textoFormateado = String.format(
                            "Evento: %s | Nombre: %s | Tipo: %s | Ciudad: %s | Fecha: %s",
                            artista, nombreEvento, tipo, ciudad, fecha
                    );
                    eventsTextArea.append(textoFormateado + "\n");
                }
            }
            if (!hayResultados) {
                eventsTextArea.setText("No se encontraron eventos para la ciudad seleccionada.");
            }
        } catch (SQLException e) {
            eventsTextArea.setText("Error al acceder a la base de datos.");
            e.printStackTrace();
        }
    }private void searchFlights() {
        flightsTextArea.setText("");
        try {
            List<String> vuelos = datamartManager.getAllFlights();
            String origen = (String) originComboBox.getSelectedItem();
            String destino = (String) destinationComboBox.getSelectedItem();

            if (origen.equals(destino)) {
                flightsTextArea.setText("El origen y destino no pueden ser iguales.");
                return;
            } boolean hayResultados = false;
            for (String vuelo : vuelos) {
                if (vuelo.contains(origen + " -> " + destino)) {
                    flightsTextArea.append(vuelo + "\n");
                    hayResultados = true;
                }
            }
            if (!hayResultados) {
                flightsTextArea.setText("No se encontraron vuelos para el origen y destino seleccionados.");
            }
        } catch (SQLException e) {
            flightsTextArea.setText("Error al acceder a la base de datos.");
            e.printStackTrace();
        }
    }
}