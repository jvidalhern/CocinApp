package vidal.juan.cocinapp;

public class EncapsuladorEntradas {

    private int imagen;
    private String titulo;
    private String precio;
    private int cantidadActual;

    private int cantidadMaxima;

    private int stock;


    public EncapsuladorEntradas(int idImagen, String tituloEntrada, String precioEntrada, int cantidadMaxima, int stock) {
        this.imagen = idImagen;
        this.titulo = tituloEntrada;
        this.precio = precioEntrada;
        this.cantidadMaxima = cantidadMaxima;
        this.stock = stock;
    }

    public String get_textoTitulo() {
        return titulo;
    }

    public String get_Precio() {
        return precio;
    }

    public int get_idImagen() {
        return imagen;
    }

    public int getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(int cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    public int getCantidadMaxima() {
        return cantidadMaxima;
    }

    public void setCantidadMaxima(int cantidadMaxima) {
        this.cantidadMaxima = cantidadMaxima;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
