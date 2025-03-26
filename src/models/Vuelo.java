package models;

public class Vuelo {
    private String id;
    private String origen;
    private String destino;
    private String fechaSalida;
    private double precio;

    public Vuelo(String id, String oringen, String destino, String fechaSalida, double precio) {
        this.id = id;
        this.origen = oringen;
        this.destino = destino;
        this.fechaSalida = fechaSalida;
        this.precio = precio;
        }
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getOringen() {return origen;}
    public void setOringen(String origen) {this.origen = origen;}

    public String getDestino() {return destino;}
    public void setDestino(String destino) {this.destino = destino;}

    public String getFechaSalida() {return fechaSalida;}
    public void setFechaSalida(String fechaSalida) {this.fechaSalida = fechaSalida;}

    public double getPrecio() {return precio;}
    public void setPrecio(double precio) {this.precio = precio;}

    @Override
    public String toString() {
        return "Vuelo ID: " + id + " | " + origen + " -> " + destino + " | Fecha: " + fechaSalida + " | Precio: €" + precio;
    }
}
