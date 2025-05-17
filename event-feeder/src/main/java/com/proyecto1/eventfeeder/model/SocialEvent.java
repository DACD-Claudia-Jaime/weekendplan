package com.proyecto1.eventfeeder.model;

import java.time.Instant;

public class SocialEvent {
    private Instant ts;
    private String ss;
    private String nombre;
    private String ciudad;
    private String fecha;

    public SocialEvent(Instant ts, String ss, String nombre, String ciudad, String fecha) {
        this.ts = ts;
        this.ss = ss;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.fecha = fecha;
    }

    public String getTs() {
        return ts.toString();
    }

    public String getSs() {
        return ss;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        return "Evento: " + nombre + " | Ciudad: " + ciudad + " | Fecha: " + fecha;
    }
}