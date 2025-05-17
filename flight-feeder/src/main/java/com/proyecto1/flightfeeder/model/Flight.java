package com.proyecto1.flightfeeder.model;

import java.time.Instant;

public class Flight {
    private Instant ts;
    private String ss;
    private String origen;
    private String destino;
    private String fecha;
    private double precio;

    public Flight(Instant ts, String ss, String origen, String destino, String fecha, double precio) {
        this.ts = ts;
        this.ss = ss;
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.precio = precio;
    }
    public Instant getTs() {
        return ts;
    }
    public String getSs() {
        return ss;
    }
    public String getOrigen() {
        return origen;
    }
    public String getDestino() {
        return destino;
    }
    public String getFecha() {
        return fecha;
    }
    public double getPrecio() {
        return precio;
    }
    @Override
    public String toString() {
        return "Vuelo de " + origen + " a " + destino +
                " | Fecha: " + fecha +
                " | Precio: €" + precio;
    }
}