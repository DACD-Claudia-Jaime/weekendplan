package com.proyecto1.flightfeeder.controller;

import com.proyecto1.flightfeeder.model.Flight;
import java.util.List;

public interface FlightFinder {
    List<Flight> find(String origen, String destino);
}