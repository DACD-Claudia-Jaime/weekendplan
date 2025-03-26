package api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import models.Vuelo;

import java.util.ArrayList;
import java.util.List;


public class AmadeusApi {
    private final Amadeus amadeus;
    private static final Logger logger = LogManager.getLogger(AmadeusApi.class);
    public AmadeusApi() {
        amadeus = Amadeus.builder("mWDBAs4H8kdq2VNAzDw87mA1p0oFDwLe", "AaygDxAVj5QwCiXk").build();
    }

    // Vuelos
    public List<Vuelo> buscarVuelos(String origen, String destino, String fecha) {
        List<Vuelo> listaVuelos = new ArrayList<>();
        try {
            FlightOfferSearch[] vuelos = amadeus.shopping.flightOffersSearch.get(
                    Params.with("originLocationCode", origen)
                            .and("destinationLocationCode", destino)
                            .and("departureDate", fecha)
                            .and("adults", 1)
                            .and("max", 5) // 5 resultados como máximo
            );
            for (FlightOfferSearch vuelo : vuelos) {
                Vuelo v = new Vuelo(
                        vuelo.getId(),
                        origen,
                        destino,
                        fecha,
                        Double.parseDouble(vuelo.getPrice().getTotal())
                );
                listaVuelos.add(v);
            }
        } catch (ResponseException e) {
            logger.error("Error al buscar los vuelos", e);
        }
        return listaVuelos;
    }
}