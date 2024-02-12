package vidal.juan.cocinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VerDetallesPedidoActivity extends AppCompatActivity {

    private Button volverPedidosButton;
    private ListView listaDetalle;
    private TextView fechaPedidoDetalle,fechaPedidoEntregaDetalle,cometariosPedidoDEtalle,totaPedidoDetalle;
    private String idPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_detalles_pedido);
        //referencia a items xml
        volverPedidosButton = findViewById(R.id.volverPedidosButton);
        listaDetalle = findViewById(R.id.listaDetalle);
        fechaPedidoDetalle = findViewById(R.id.fechaPedidoDetalle);
        fechaPedidoEntregaDetalle = findViewById(R.id.fechaPedidoEntregaDetalle);
        cometariosPedidoDEtalle = findViewById(R.id.cometariosPedidoDEtalle);
        totaPedidoDetalle = findViewById(R.id.totaPedidoDetalle);

        //Volver a la pantalla principal
        volverPedidosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volverPedidos();
            }
        });


        // Recuperar detalles seleccionados y precio total de la anterior actividad
        idPedido = getIntent().getStringExtra("idPedido");
        Log.d("PedidoRecib", "Pedido: " + idPedido);
        //buscar y mostrar los detalles del pedido a partir de id
        buscarPedido(idPedido);

    }

    private void buscarPedido(String idPedido) {
        // Referencia a la base de datos mediante el id pedido
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("pedidos").child(idPedido);
        Log.d("PedidoBuscar", "Pedido buscado : " + idPedido);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    Pedido pedido = dataSnapshot.getValue(Pedido.class);
                    Log.d("PedidoEncontrado", "Pedido encontrado por id: " + pedido.toString());
                    //Pasar el pedido a lista
                    llenarLista(pedido);
                    fechaPedidoDetalle.setText(getString(R.string.fecha_pedido) + "\n " + pedido.getFecha_pedido().toString());
                    fechaPedidoEntregaDetalle.setText(getString(R.string.fechaEntrega)+ "\n " + pedido.getFecha_entrega().toString());
                    cometariosPedidoDEtalle.setText(getString(R.string.comentarios)+ "\n " + pedido.getComentarios().toString());
                    totaPedidoDetalle.setText(String.valueOf(getString(R.string.total)+ "\n " + pedido.getPrecio_total()) + "\u20AC" );

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de la base de datos
                Log.e("Error BBDD", "Error al buscar el pedido: " + databaseError.getMessage());
            }
        });
    }

    private void llenarLista(Pedido pedido){
        listaDetalle.setAdapter(new AdaptadorDetallesNoparcel(VerDetallesPedidoActivity.this, R.layout.detalle_pedido_vista, pedido.getDetalles()) {
            @Override
            public void onEntrada(DetallePedidoNoParcel detallePedido, View view) {
                if (detallePedido != null) {
                    TextView nombreRacionDetalle = view.findViewById(R.id.nombreRacionDetalle);
                    TextView cantidadRacionDetalleVistaDetalle = view.findViewById(R.id.cantidadRacionDetalleVistaDetalle);
                    TextView cantidadRacionVistaDetalle = view.findViewById(R.id.cantidadRacionVistaDetalle);
                    TextView precioRacionDetalleVistaDetalle = view.findViewById(R.id.precioRacionDetalleVistaDetalle);

                    nombreRacionDetalle.setText(detallePedido.getRacion());
                    cantidadRacionDetalleVistaDetalle.setText(String.valueOf(detallePedido.getCantidad()));
                    precioRacionDetalleVistaDetalle.setText(String.valueOf (detallePedido.getPrecio()) + "\u20AC");


                }
            }
        });

    }

    /**
     * Volver PPrincipal
     */
    private  void volverPedidos() {
        Intent intent = new Intent(VerDetallesPedidoActivity.this, VerPedidoActivity.class);
        startActivity(intent);
        finish();
    }
}