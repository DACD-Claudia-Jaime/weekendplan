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
        if (ciudadSeleccionada == null || ciudadSeleccionada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una ciudad.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<String> eventos = datamartManager.getAllSocialEvents();
            boolean hayResultados = false;

            for (String evento : eventos) {
                if (evento.toLowerCase().contains(ciudadSeleccionada.toLowerCase())) {
                    String[] parts = evento.split("\\|");

                    String artista = "(sin artista)";
                    String nombreEvento = "(sin nombre)";
                    String tipo = "(sin tipo)";
                    String ciudad = "(sin ciudad)";
                    String fecha = "(sin fecha)";

                    if (parts.length >= 5) {
                        String[] artistaNombre = parts[0].split(" - ", 2);
                        if (artistaNombre.length >= 1 && !artistaNombre[0].trim().isEmpty()) {
                            artista = artistaNombre[0].trim();
                        }
                        if (artistaNombre.length == 2 && !artistaNombre[1].trim().isEmpty()) {
                            nombreEvento = artistaNombre[1].trim();
                        }
                        if (!parts[1].trim().isEmpty()) tipo = parts[1].trim();
                        if (!parts[2].trim().isEmpty()) ciudad = parts[2].trim();
                        if (!parts[3].trim().isEmpty()) fecha = parts[3].trim();

                    }

                    if (
                            artista.equals("(sin artista)") &&
                                    nombreEvento.equals("(sin nombre)") &&
                                    tipo.equals("(sin tipo)") &&
                                    ciudad.equals("(sin ciudad)") &&
                                    fecha.equals("(sin fecha)")
                    ) {
                        continue; // ignorar
                    }

                    String textoFormateado = String.format(
                            "Evento: %s | Nombre: %s | Tipo: %s | Ciudad: %s | Fecha: %s",
                            artista, nombreEvento, tipo, ciudad, fecha
                    );

                    eventsTextArea.append(textoFormateado + "\n");
                    hayResultados = true;
                }
            }

            if (!hayResultados) {
                eventsTextArea.setText("No se encontraron eventos para la ciudad seleccionada.");
            }
        } catch (SQLException e) {
            eventsTextArea.setText("Error al acceder a la base de datos.");
            e.printStackTrace();
        }
    }
    private void searchFlights() {
        flightsTextArea.setText("");
        try {
            List<String> vuelos = datamartManager.getAllFlights();
            String origen = (String) originComboBox.getSelectedItem();
            String destino = (String) destinationComboBox.getSelectedItem();

            if (origen.equals(destino)) {
                flightsTextArea.setText("El origen y destino no pueden ser iguales.");
                return;
            }

            boolean hayResultados = false;
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