package com.proyecto1.eventfeeder.controller;

import com.proyecto1.eventfeeder.model.SocialEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TicketMasterEventFinder implements EventFinder {
    private final String apiKey;

    public TicketMasterEventFinder(String apiKey, String secret) {
        this.apiKey = apiKey;
    }

    public List<SocialEvent> find(String city) {
        List<SocialEvent> listaEvents = new ArrayList<>();
        try {
            String ciudadCodificada = URLEncoder.encode(city, StandardCharsets.UTF_8);
            String url = "https://app.ticketmaster.com/discovery/v2/events.json?city=" + ciudadCodificada + "&apikey=" + apiKey;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Respuesta de Ticketmaster:");
            System.out.println(response.body());
            JSONObject jsonResponse = new JSONObject(response.body());
            if (jsonResponse.has("_embedded")) {
                JSONArray eventos = jsonResponse.getJSONObject("_embedded").getJSONArray("events");
                for (int i = 0; i < eventos.length(); i++) {
                    JSONObject eventoJson = eventos.getJSONObject(i);
                    String nombre = eventoJson.getString("name");
                    String fecha = eventoJson.getJSONObject("dates").getJSONObject("start").getString("localDate");
                    String ciudad = "Desconocida";
                    if (eventoJson.has("_embedded")) {
                        JSONObject embedded = eventoJson.getJSONObject("_embedded");
                        if (embedded.has("venues")) {
                            JSONArray venues = embedded.getJSONArray("venues");
                            if (venues.length() > 0) {
                                JSONObject venue = venues.getJSONObject(0);
                                if (venue.has("city")) {
                                    ciudad = venue.getJSONObject("city").getString("name");
                                }
                            }
                        }
                    }
                    SocialEvent event = new SocialEvent(Instant.now(), "ticketmaster", nombre, ciudad, fecha);
                    listaEvents.add(event);
                }
            } else {
                System.out.println("No se encontraron eventos para: " + city);
            }
        } catch (Exception e) {
            System.err.println("Error al buscar eventos en Ticketmaster:");
            e.printStackTrace();
        }
        return listaEvents;
    }
}