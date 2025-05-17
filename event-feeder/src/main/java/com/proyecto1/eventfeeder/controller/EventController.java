package com.proyecto1.eventfeeder.controller;

import com.proyecto1.eventfeeder.model.SocialEvent;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EventController {
    private final List<String> cities;
    private final EventFinder eventFinder;
    private final EventStore store;

    public EventController(List<String> cities, EventFinder finder, EventStore store) {
        this.cities = cities;
        this.eventFinder = finder;
        this.store = store;
    }
    public void start() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            for (String city : cities) {
                List<SocialEvent> eventos = eventFinder.find(city);
                System.out.println("[" + city + "] Eventos encontrados: " + eventos.size());
                for (SocialEvent e : eventos) {
                    System.out.println(" Guardando evento: "
                            + e.getNombre()
                            + " | Fecha: " + e.getFecha());
                    store.saveEvent(e);
                }
            }
        }, 0, 1, TimeUnit.HOURS);
    }
}