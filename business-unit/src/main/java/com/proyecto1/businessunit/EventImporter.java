package com.proyecto1.businessunit;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.stream.Stream;

public class EventImporter {
    private final DatamartManager datamartManager;
    private final Gson gson = new Gson();
    public EventImporter(DatamartManager datamartManager) {
        this.datamartManager = datamartManager;
    }
    public void importSocialEvents() throws IOException, SQLException {
        Path dir = Paths.get("eventstore", "TicketmasterEvents", "ticketmaster");
        if (!Files.exists(dir)) {
            System.out.println("No existe la carpeta de eventos sociales para importar.");
            return;
        }
        try (Stream<Path> files = Files.list(dir)) {
            files.filter(f -> f.toString().endsWith(".events")).forEach(this::importSocialEventsFromFile);
        }
    }
    private void importSocialEventsFromFile(Path file) {
        try {
            Files.lines(file).forEach(line -> {
                try {
                    JsonObject jsonObject = gson.fromJson(line, JsonObject.class);
                    datamartManager.insertSocialEvent(
                            jsonObject.get("ts").getAsString(),
                            jsonObject.get("ss").getAsString(),
                            jsonObject.get("nombre").getAsString(),
                            jsonObject.get("ciudad").getAsString(),
                            jsonObject.get("fecha").getAsString(),
                            jsonObject.get("precio").getAsDouble()
                    );
                } catch (SQLException e) {
                    System.err.println("Error al insertar evento en la base de datos: " + e.getMessage());
                }
            });
            System.out.println("Importados eventos desde: " + file);
        } catch (IOException e) {
            System.err.println("Error leyendo archivo: " + file);
            e.printStackTrace();
        }
    }
    public void importFlights() throws IOException, SQLException {
        Path dir = Paths.get("eventstore", "AmadeusFlights", "ticketmaster");
        if (!Files.exists(dir)) {
            System.out.println("No existe la carpeta de vuelos para importar.");
            return;
        }
        try (Stream<Path> files = Files.list(dir)) {
            files.filter(f -> f.toString().endsWith(".events")).forEach(this::importFlightsFromFile);
        }
    }
    private void importFlightsFromFile(Path file) {
        try {
            Files.lines(file).forEach(line -> {
                try {
                    JsonObject jsonObject = gson.fromJson(line, JsonObject.class);
                    datamartManager.insertFlight(
                            jsonObject.get("ts").getAsString(),
                            jsonObject.get("ss").getAsString(),
                            jsonObject.get("origen").getAsString(),
                            jsonObject.get("destino").getAsString(),
                            jsonObject.get("fecha").getAsString(),
                            jsonObject.get("precio").getAsDouble()
                    );
                } catch (SQLException e) {
                    System.err.println("Error al insertar vuelo en la base de datos: " + e.getMessage());
                }
            });
            System.out.println("Importados vuelos desde: " + file);
        } catch (IOException e) {
            System.err.println("Error leyendo archivo: " + file);
            e.printStackTrace();
        }
    }
}