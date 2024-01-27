package vidal.juan.cocinapp;

/**
 * Clase que representa la realidad de los detalles de un pedido
 */
public class DetallePedido {

    private int cantidad;
    private double precio;
    private String racion;


    public DetallePedido(int cantidad, double precio, String racion) {
        this.cantidad = cantidad;
        this.precio = precio;
        this.racion = racion;
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

    public String getRacion() {
        return racion;
    }

    public void setRacion(String racion) {
        this.racion = racion;
    }

    @Override
    public String toString() {
        return "DetallePedido{" +
                "cantidad=" + cantidad +
                ", precio=" + precio +
                ", racion='" + racion + '\'' +
                '}';
    }
}