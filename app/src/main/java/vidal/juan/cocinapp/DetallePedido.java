package vidal.juan.cocinapp;

/**
 * Clase que representa la realidad de los detalles de un pedido
 */
import android.os.Parcel;
import android.os.Parcelable;

public class DetallePedido implements Parcelable {
    private String nombreRacion;
    private int cantidad;
    private double precio;

    // Constructor necesario para Parcelable
    private DetallePedido(Parcel in) {
        nombreRacion = in.readString();
        cantidad = in.readInt();
        precio = in.readDouble();
    }
    //Constructor para firebase
    public DetallePedido() {
    }

    public DetallePedido(String nombreRacion, int cantidad, double precio) {
        this.nombreRacion = nombreRacion;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    //GET/SET
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

    //Tostring
    @Override
    public String toString() {
        return "DetallePedido{" +
                "cantidad=" + cantidad +
                ", precio=" + precio +
                ", racion='" + nombreRacion + '\'' +
                '}';
    }

    // Métodoss Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombreRacion);
        dest.writeInt(cantidad);
        dest.writeDouble(precio);
    }

    public static final Creator<DetallePedido> CREATOR = new Creator<DetallePedido>() {
        @Override
        public DetallePedido createFromParcel(Parcel in) {
            return new DetallePedido(in);
        }

        @Override
        public DetallePedido[] newArray(int size) {
            return new DetallePedido[size];
        }
    };
}