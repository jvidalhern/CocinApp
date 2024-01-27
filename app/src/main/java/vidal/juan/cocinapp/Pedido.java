package vidal.juan.cocinapp;

import java.util.List;

/**
 * Clase que represnta la realidad de un pedido, este podria teneer varios detalles de pedido
 */
public class Pedido {

    private String comentarios;
    private List<DetallePedido> detalles;
    private String estado;
    private String fechaPedido;
    private String fechaEntrega;
    private double precioTotal;
    private String usuarioId;


    public Pedido() {
        // Constructor vacío requerido por Firebase
    }

    public Pedido(String comentarios, List<DetallePedido> detalles, String estado, String fechaPedido, String fechaEntrega, double precioTotal, String usuarioId) {
        this.comentarios = comentarios;
        this.detalles = detalles;
        this.estado = estado;
        this.fechaPedido = fechaPedido;
        this.fechaEntrega = fechaEntrega;
        this.precioTotal = precioTotal;
        this.usuarioId = usuarioId;
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

    public String getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
}
