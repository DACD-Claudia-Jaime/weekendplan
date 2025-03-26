package api;

import java.nio.charset.StandardCharsets;
import models.Evento;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class TicketmasterApi {
    private static final String API_KEY = "YG9rAaAyDiMXFGkn1cphXVNUtsokhtIM";
    public List<Evento> buscarEventos(String ciudad) {
        List<Evento> listaEventos = new ArrayList<>();
        String ciudadCodificada = URLEncoder.encode(ciudad, StandardCharsets.UTF_8);
        String url = "https://app.ticketmaster.com/discovery/v2/events.json?city=" + ciudad + "&apikey=" + API_KEY;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonResponse = new JSONObject(response.body());
            if (jsonResponse.has("_embedded")){
                JSONArray eventos = jsonResponse.getJSONObject("_embedded").getJSONArray("events");

                for (int i = 0; i < eventos.length(); i++){
                    JSONObject eventoJson = eventos.getJSONObject(i);
                    String id = eventoJson.getString("id");
                    String nombre = eventoJson.getString("name");
                    String fecha = eventoJson.getJSONObject("dates").getJSONObject("start").getString("localDate");
                    double precio = eventoJson.has("priceRanges")?
                            eventoJson.getJSONArray("priceRanges").getJSONObject(0).getDouble("min") : 0.0;
                    Evento e = new Evento(id, nombre, ciudad, fecha, precio);
                    listaEventos.add(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEventos;
    }
}
