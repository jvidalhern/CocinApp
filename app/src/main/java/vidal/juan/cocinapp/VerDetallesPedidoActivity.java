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

public class VerDetallesPedidoActivity extends AppCompatActivity {

    private Button volverPedidosButton,modPediddoButton,eliminarPedidoButton;
    private ListView listaDetalle;
    private TextView fechaPedidoDetalleText,fechaEntregaDetalleText,cometariosDetalleText,totalDetalleText,idPedidoTextView,textModPedidoInfo;
    private String idPedido;
    private LinearLayout layoutEditarPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ActivityLifecycle", "onCreate() VerDetalles");
        setContentView(R.layout.activity_ver_detalles_pedido);
        //referencia a items xml
        volverPedidosButton = findViewById(R.id.volverPedidosButton);
        modPediddoButton = findViewById(R.id.modPediddoButton);
        eliminarPedidoButton = findViewById(R.id.eliminarPedidoButton);
        listaDetalle = findViewById(R.id.listaDetalle);
        fechaPedidoDetalleText = findViewById(R.id.fechaPedidoDetalleText);
        fechaEntregaDetalleText = findViewById(R.id.fechaEntregaDetalleText);
        cometariosDetalleText = findViewById(R.id.cometariosDetalleText);
        totalDetalleText = findViewById(R.id.totalDetalleText);
        idPedidoTextView  = findViewById(R.id.idPedidoTextView);
        textModPedidoInfo  = findViewById(R.id.textModPedidoInfo);
        layoutEditarPedido = findViewById(R.id.layoutModPedido);
        //Funcionalidad botones
        //Volver a la pantalla principal
        volverPedidosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volverPedidos();
            }
        });
        //Eliminar pedido
        eliminarPedidoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarPedido();
            }
        });
        //Modificar pedido
        modPediddoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarPedido();
            }
        });


        // Recuperar detalles seleccionados y precio total de la anterior actividad
        idPedido = getIntent().getStringExtra("idPedido");
        Log.d("PedidoRecib", "Pedido: " + idPedido);
        //buscar y mostrar los detalles del pedido a partir de id

        buscarPedido(idPedido);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ActivityLifecycle", "onDestroy() VerDetalles");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ActivityLifecycle", "onPause() Verdetalles");

    }

    private void modificarPedido() {
        Intent modPedido = new Intent(VerDetallesPedidoActivity.this, ModificarPedidoActivity.class);
        modPedido.putExtra("idPedido", idPedido);
        startActivity(modPedido);
        finish();
    }

    private void eliminarPedido() {
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
                    if (pedido!= null){
                    Log.d("PedidoEncontrado", "Pedido encontrado por id: " + pedido.toString());
                    //Pasar el pedido a lista
                    llenarLista(pedido);
                    fechaPedidoDetalleText.setText(pedido.getFecha_pedido().toString());
                    fechaEntregaDetalleText.setText(pedido.getFecha_entrega().toString());
                    cometariosDetalleText.setText(pedido.getComentarios().toString());
                    totalDetalleText.setText(String.valueOf(pedido.getPrecio_total()) + "\u20AC");
                    idPedidoTextView.setText(getString(R.string.idPedidoString) + idPedido.substring(3, 7));
                    //Controlar editar el peddio en funcion de si el pedido es editable
                    //Cambiar color y la visibilidad en funcion del estado
                    if (pedido.getEditable() == true)
                        layoutEditarPedido.setVisibility(View.VISIBLE);
                    else {
                        textModPedidoInfo.setBackgroundColor(getResources().getColor(R.color.defPedidoColor));
                        textModPedidoInfo.setText(getString(R.string.noModPedidoMensaje));
                        layoutEditarPedido.setVisibility(View.GONE);
                    }
                    }
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
     * Volver pedidos
     */
    private  void volverPedidos() {
        Intent intent = new Intent(VerDetallesPedidoActivity.this, VerPedidoActivity.class);
        startActivity(intent);
        finish();
    }
}