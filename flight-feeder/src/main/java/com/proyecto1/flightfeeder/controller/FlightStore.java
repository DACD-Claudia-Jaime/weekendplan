package com.proyecto1.flightfeeder.controller;

import com.proyecto1.flightfeeder.model.Flight;

public interface FlightStore {
    void saveFlight(Flight flight);
}