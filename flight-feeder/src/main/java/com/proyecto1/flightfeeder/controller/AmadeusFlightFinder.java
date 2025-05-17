package com.proyecto1.flightfeeder.controller;

import com.proyecto1.flightfeeder.model.Flight;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AmadeusFlightFinder implements FlightFinder {
    private final String token;

    public AmadeusFlightFinder(String token) {
        this.token = token;
    }
    @Override
    public List<Flight> find(String origen, String destino) {
        List<Flight> vuelos = new ArrayList<>();

        try {
            String url = "https://test.api.amadeus.com/v2/shopping/flight-offers?" +
                    "originLocationCode=" + origen +
                    "&destinationLocationCode=" + destino +
                    "&departureDate=2025-06-10&adults=1&max=5";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .header("Authorization", "Bearer " + token)
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONArray ofertas = jsonResponse.getJSONArray("data");

            for (int i = 0; i < ofertas.length(); i++) {
                JSONObject oferta = ofertas.getJSONObject(i);
                JSONArray itinerarios = oferta.getJSONArray("itineraries");
                JSONObject itinerario = itinerarios.getJSONObject(0);
                String fecha = itinerario.getJSONArray("segments")
                        .getJSONObject(0)
                        .getJSONObject("departure")
                        .getString("at")
                        .split("T")[0];

                double precio = oferta.getJSONObject("price").getDouble("total");

                Flight vuelo = new Flight(
                        Instant.now(),
                        "amadeus",
                        origen,
                        destino,
                        fecha,
                        precio
                );
                vuelos.add(vuelo);
            }
        } catch (Exception e) {
            System.err.println(" Error al consultar vuelos:");
            e.printStackTrace();
        }
        return vuelos;
    }
}