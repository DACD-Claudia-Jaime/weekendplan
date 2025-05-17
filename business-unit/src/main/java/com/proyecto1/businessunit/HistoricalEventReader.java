package com.proyecto1.businessunit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HistoricalEventReader {

    private static final String BASE_PATH = "eventstore";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private String getTodayFileName() {
        return LocalDate.now().format(DATE_FORMAT) + ".events";
    }

    public List<String> leerEventos(String ciudad) {
        List<String> eventos = new ArrayList<>();
        String filePath = BASE_PATH + "/TicketmasterEvents/ticketmaster/" + getTodayFileName();

        File file = new File(filePath);
        System.out.println("Leyendo archivo de eventos: " + file.getAbsolutePath());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JsonObject jsonObj = JsonParser.parseString(line).getAsJsonObject();
                String ciudadData = jsonObj.has("ciudad") ? jsonObj.get("ciudad").getAsString() : "";
                if (ciudadData.equalsIgnoreCase(ciudad)) {
                    String nombre = jsonObj.has("nombre") ? jsonObj.get("nombre").getAsString() : "N/D";
                    String fecha = jsonObj.has("fecha") ? jsonObj.get("fecha").getAsString() : "N/D";
                    eventos.add(nombre + " | " + ciudadData + " | " + fecha);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer los eventos: " + e.getMessage());
        }
        return eventos;
    }

    public List<String> leerVuelos(String origen, String destino) {
        List<String> vuelos = new ArrayList<>();
        String filePath = BASE_PATH + "/AmadeusFlights/amadeus/" + getTodayFileName();

        File file = new File(filePath);
        System.out.println("Leyendo archivo de vuelos: " + file.getAbsolutePath());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JsonObject jsonObj = JsonParser.parseString(line).getAsJsonObject();
                String origenData = jsonObj.has("origen") ? jsonObj.get("origen").getAsString() : "";
                String destinoData = jsonObj.has("destino") ? jsonObj.get("destino").getAsString() : "";
                if (origenData.equalsIgnoreCase(origen) && destinoData.equalsIgnoreCase(destino)) {
                    String fecha = jsonObj.has("fecha") ? jsonObj.get("fecha").getAsString() : "N/D";
                    double precio = jsonObj.has("precio") ? jsonObj.get("precio").getAsDouble() : 0.0;
                    vuelos.add(String.format("%s -> %s | %s | €%.2f", origenData, destinoData, fecha, precio));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer los vuelos: " + e.getMessage());
        }
        return vuelos;
    }
}