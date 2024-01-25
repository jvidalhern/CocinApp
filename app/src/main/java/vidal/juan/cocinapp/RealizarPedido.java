package vidal.juan.cocinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * Una vez vez seleccionado el menu se hace el pedido eligiendo la fecha de entrega, no se podra elegir fechas anterires a la de hoy ni posteriores a x dias,  en un calendario
 * Cuando se registre el pedido se podra modificar el un periodo de x dias
 */
public class RealizarPedido extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_pedido);
    }
}