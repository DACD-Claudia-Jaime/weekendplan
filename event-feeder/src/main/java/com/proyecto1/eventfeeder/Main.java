package com.proyecto1.eventfeeder;

import com.proyecto1.eventfeeder.controller.ActiveMQEventStore;
import com.proyecto1.eventfeeder.controller.EventController;
import com.proyecto1.eventfeeder.controller.TicketMasterEventFinder;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Uso: <apiKey> <secret> <ciudades_coma_separadas>");
            System.exit(1);
        }
        String apiKey = args[0];
        String secret = args[1];
        String citiesArg = args[2];
        List<String> cities = Arrays.asList(citiesArg.split(","));

        String brokerUrl = "tcp://localhost:61616";
        String topic = "TicketmasterEvents";
        ActiveMQEventStore store = new ActiveMQEventStore(brokerUrl, topic);
        TicketMasterEventFinder finder = new TicketMasterEventFinder(apiKey, secret);
        EventController controller = new EventController(cities, finder, store);
        controller.start();
    }
}