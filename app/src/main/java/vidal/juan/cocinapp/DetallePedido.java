package vidal.juan.cocinapp;

/**
 * Clase que representa la realidad de los detalles de un pedido
 */
public class DetallePedido {
    private String nombreRacion;
    private int cantidad;
    private double precio;


    public DetallePedido() {
    }

    public DetallePedido(String nombreRacion, int cantidad, double precio) {
        this.nombreRacion = nombreRacion;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getNombreRacion() {
        return nombreRacion;
    }

    public void setNombreRacion(String nombreRacion) {
        this.nombreRacion = nombreRacion;
    }

    @Override
    public String toString() {
        return "DetallePedido{" +
                "cantidad=" + cantidad +
                ", precio=" + precio +
                ", racion='" + nombreRacion + '\'' +
                '}';
    }
}