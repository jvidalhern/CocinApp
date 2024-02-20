package vidal.juan.cocinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ModificarPedidoActivity extends AppCompatActivity {

    private Button volverDetallesPedidosButton,confirmModPedidoButton;
    private ListView listaDetalleMod;
    private TextView fechaPedidoDetalleTextMod,fechaEntregaModTextview,cometariosDetalleTextMod,totalDetalleTextMod,idPedidoModTextView;
    private String idPedido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_pedido);
        //referencia a items xml
        volverDetallesPedidosButton = findViewById(R.id.volverDetallesPedidosButton);
        confirmModPedidoButton = findViewById(R.id.confirmModPedidoButton);

        listaDetalleMod = findViewById(R.id.listaDetalleMod);
        fechaPedidoDetalleTextMod = findViewById(R.id.fechaPedidoDetalleTextMod);
        fechaEntregaModTextview = findViewById(R.id.fechaEntregaModTextview);
        cometariosDetalleTextMod = findViewById(R.id.cometariosDetalleTextMod);
        totalDetalleTextMod = findViewById(R.id.totalDetalleTextMod);
        idPedidoModTextView  = findViewById(R.id.idPedidoModTextView);

        //Funcionalidad botones
        //Volver a la pantalla principal
        volverDetallesPedidosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volverDetallePedido();
            }
        });

        //Modificar pedido
        confirmModPedidoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmModificarPedido();
            }
        });


        // Recuperar detalles seleccionados y precio total de la anterior actividad
        idPedido = getIntent().getStringExtra("idPedido");
        Log.d("PedidoRecib", "Pedido: " + idPedido);
        //buscar y mostrar los detalles del pedido a partir de id
        buscarPedido(idPedido);

    }

    private void confirmModificarPedido() {
    }



    /**
     * Buscar el pedido por el id de la ativity anterior
     * @param idPedido id del pedido obtenido en la activity anterior
     */
    private void buscarPedido(String idPedido) {
        // Referencia a la base de datos mediante el id pedido
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("pedidos").child(idPedido);
        Log.d("PedidoBuscar", "Pedido buscado : " + idPedido);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Obj Pedido encontrado a partir del Idpedido
                Pedido pedido = dataSnapshot.getValue(Pedido.class);
                Log.d("PedidoEncontrado", "Pedido encontrado por id: " + pedido.toString());
                //Pasar el pedido a lista
                llenarLista(pedido);
                fechaPedidoDetalleTextMod.setText(pedido.getFecha_pedido().toString());
                fechaEntregaModTextview.setText( pedido.getFecha_entrega().toString());
                cometariosDetalleTextMod.setText(pedido.getComentarios().toString());
                totalDetalleTextMod.setText(String.valueOf(pedido.getPrecio_total()) + "\u20AC" );
                idPedidoModTextView.setText(getString(R.string.idPedidoString) + idPedido.substring(3,7));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de la base de datos
                Log.e("Error BBDD", "Error al buscar el pedido: " + databaseError.getMessage());
            }
        });
    }

    /**
     * Llenar la lista con los detalles del pedido
     * @param pedido objeto pedido del que se obtienen los detalles
     */
    private void llenarLista(Pedido pedido){
        listaDetalleMod.setAdapter(new AdaptadorDetallesNoparcel(ModificarPedidoActivity.this, R.layout.detealle_pedido_mod_vista, pedido.getDetalles()) {
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
     * Volver Detalles del pedido
     */
    private  void volverDetallePedido() {
        Intent intent = new Intent(ModificarPedidoActivity.this, VerDetallesPedidoActivity.class);
        intent.putExtra("idPedido", idPedido);//Pasar el id del detalle del peddido para volver a donde estaba
        startActivity(intent);

        finish();
    }
}