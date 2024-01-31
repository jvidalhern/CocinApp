package vidal.juan.cocinapp;

import java.util.List;

/**
 * Clase que represnta la realidad de un pedido, este podria teneer varios detalles de pedido
 */
public class Pedido {

    private String comentarios;
    private List<DetallePedido> detalles;
    private String estado;
    private String fecha_pedido;
    private String fecha_entrega;
    private double precio_total;
    private String usuario;


    public Pedido() {
        // Constructor vacío requerido por Firebase
    }

    public Pedido(String comentarios, List<DetallePedido> detalles, String estado, String fecha_pedido, String fecha_entrega, double precio_total, String usuario) {
        this.comentarios = comentarios;
        this.detalles = detalles;
        this.estado = estado;
        this.fecha_pedido = fecha_pedido;
        this.fecha_entrega = fecha_entrega;
        this.precio_total = precio_total;
        this.usuario = usuario;
    }


    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha_pedido() {
        return fecha_pedido;
    }

    public void setFecha_pedido(String fecha_pedido) {
        this.fecha_pedido = fecha_pedido;
    }

    public String getFecha_entrega() {
        return fecha_entrega;
    }

    public void setFecha_entrega(String fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }

    public double getPrecio_total() {
        return precio_total;
    }

    public void setPrecio_total(double precio_total) {
        this.precio_total = precio_total;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "comentarios='" + comentarios + '\'' +
                ", detalles=" + detalles +
                ", estado='" + estado + '\'' +
                ", fecha_pedido='" + fecha_pedido + '\'' +
                ", fecha_entrega='" + fecha_entrega + '\'' +
                ", precio_total=" + precio_total +
                ", usuario='" + usuario + '\'' +
                '}';
    }
}
