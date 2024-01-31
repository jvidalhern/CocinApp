package vidal.juan.cocinapp;

/**
 * Clase de detalles de pedidos no parcelable, ya que firebase ignora la implemetacion en detalles de pediddo y pasa el campo stability a la BDD
 * Hay que recrear el objeto detalles sin implemetar parcel
 */
public class DetallePedidoNoParcel {
    private String racion;
    private int cantidad;
    private double precio;

    // Constructor para firebase
    public DetallePedidoNoParcel() {
    }

    public DetallePedidoNoParcel(String racion, int cantidad, double precio) {
        this.racion = racion;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    // GET/SET
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

    public String getRacion() {
        return racion;
    }

    public void setRacion(String racion) {
        this.racion = racion;
    }

    // ToString
    @Override
    public String toString() {
        return "DetallePedidoNoparcel{" +
                "cantidad=" + cantidad +
                ", precio=" + precio +
                ", racion='" + racion + '\'' +
                '}';
    }
}
