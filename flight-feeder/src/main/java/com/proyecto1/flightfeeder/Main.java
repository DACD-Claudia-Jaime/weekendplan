package com.proyecto1.flightfeeder;

import com.proyecto1.flightfeeder.controller.AmadeusFlightFinder;
import com.proyecto1.flightfeeder.controller.ActiveMQFlightStore;
import com.proyecto1.flightfeeder.controller.FlightController;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso: <token> <rutas_coma_separadas>");
            System.err.println("Ejemplo: MAD-BCN,BCN-MAD,VAL-MAD");
            System.exit(1);
        }
        String token = args[0];
        String routesArg = args[1];
        String brokerUrl = "tcp://localhost:61616";
        String topic = "AmadeusFlights";

        List<String[]> routes = new ArrayList<>();
        for (String routeStr : routesArg.split(",")) {
            String[] pair = routeStr.split("-");
            if (pair.length == 2) {
                routes.add(pair);
            }
        }
        AmadeusFlightFinder finder = new AmadeusFlightFinder(token);
        ActiveMQFlightStore store = new ActiveMQFlightStore(brokerUrl, topic);
        FlightController controller = new FlightController(routes, finder, store);
        controller.start();
    }
}