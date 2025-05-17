package com.proyecto1.flightfeeder.controller;

import com.proyecto1.flightfeeder.model.Flight;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FlightController {
    private final List<String[]> routes;
    private final FlightFinder finder;
    private final FlightStore store;
    private ScheduledExecutorService scheduler;

    public FlightController(List<String[]> routes, FlightFinder finder, FlightStore store) {
        this.routes = routes;
        this.finder = finder;
        this.store = store;
    }
    public void start() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            for (String[] route : routes) {
                String origin = route[0];
                String destination = route[1];
                List<Flight> vuelos = finder.find(origin, destination);
                System.out.println("[" + origin + "→" + destination + "] Vuelos encontrados: " + vuelos.size());
                for (Flight f : vuelos) {
                    System.out.println(" Guardando vuelo: " + f);
                    store.saveFlight(f);
                }
            }
        }, 0, 1, TimeUnit.HOURS);
    }
    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}