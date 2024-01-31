package vidal.juan.cocinapp;

/**
 * Clase que representa la realidad de los detalles de un pedido
 */
import android.os.Parcel;
import android.os.Parcelable;

public class DetallePedido implements Parcelable {
    private String racion;
    private int cantidad;
    private double precio;

    // Constructor necesario para Parcelable
    private DetallePedido(Parcel in) {
        racion = in.readString();
        cantidad = in.readInt();
        precio = in.readDouble();
    }

    // Constructor para firebase
    public DetallePedido() {
    }

    public DetallePedido(String racion, int cantidad, double precio) {
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
        return "DetallePedido{" +
                "cantidad=" + cantidad +
                ", precio=" + precio +
                ", racion='" + racion + '\'' +
                '}';
    }

    // Métodos Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(racion);
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
