package models;

public class Evento {
    private String id;
    private String nombre;
    private String ciudad;
    private String fecha;
    private double precio;

    public Evento(String id, String nombre, String ciudad, String fecha, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.fecha = fecha;
        this.precio = precio;
    }

    public String getId() {return id;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getCiudad() {return ciudad;}
    public void setCiudad(String ciudad) {this.ciudad = ciudad;}

    public String getFecha() {return fecha;}
    public void setFecha(String fecha) {this.fecha = fecha;}

    public double getPrecio() {return precio;}
    public void setPrecio(double precio) {this.precio = precio;}

    @Override
    public String toString() {
        return "Evento: " + nombre + " | Ciudad: " + ciudad + " | Fecha: " + fecha + " | Precio: € " + precio;
    }
}
