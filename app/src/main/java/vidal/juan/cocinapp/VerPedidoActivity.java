package vidal.juan.cocinapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VerPedidoActivity extends AppCompatActivity {

    private Button cancelVerPedidosActivosButton;
    private ListView listaPedidosActivos;
    private FirebaseUser usuarioLogeado ;
    private ArrayList<Pedido> pedidosActivos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_pedido);

        //referencia a items xml
        cancelVerPedidosActivosButton = findViewById(R.id.cancelVerPedidosActivosButton);
        listaPedidosActivos = findViewById(R.id.listaPedidosActivos);

        //Usuario logeado en la app
        usuarioLogeado = FirebaseAuth.getInstance().getCurrentUser();
        //volverPrpincipal boton
        //Volver a la pantalla principal
        cancelVerPedidosActivosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volverPprincipal();
            }
        });

        obtenerPedidos();

    }

    /**
     * Metodo que carga una lista de pedidos activos del usuario logeado
     *
     */
    private void obtenerPedidos() {
        // Referencia a la base de datos en tiempo real
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Realizar la consulta para obtener los pedidos del usuario con estado "preparar" o "recoger"
        databaseReference.child("pedidos").orderByChild("usuario").equalTo(usuarioLogeado.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Pedido pedido = snapshot.getValue(Pedido.class);
                            // Verificar si el pedido tiene estado "preparar" o "recoger"
                            if (pedido != null && ("preparar".equals(pedido.getEstado()) || "recoger".equals(pedido.getEstado()))) {
                                pedidosActivos.add(pedido);
                            }
                        }
                        //Prueba en log
                        for (Pedido pedido : pedidosActivos) {
                            Log.d("Pedido", "Fecha Pedido: " + pedido.getFecha_pedido() +
                                    ", Fecha Entrega: " + pedido.getFecha_entrega() +
                                    ", Estado: " + pedido.getEstado() +
                                    ", Precio: " + pedido.getPrecio_total() + "€");
                        }
                        //Llenar la lista de la vista pedidos_activos_vista.xml  con los pedidos activos obtenidos
                        listaPedidosActivos.setAdapter(new AdaptadorPedidosActivos(VerPedidoActivity.this, R.layout.pedidos_activos_vista, pedidosActivos) {
                            @Override
                            public void onEntrada(Pedido pedidoActivo, View view) {
                                if (pedidosActivos != null) {
                                    //Refencias a los elementos de la vista

                                    TextView textViewFechaPedido = view.findViewById(R.id.textViewFechaPedido);
                                    TextView textViewFechaEntrega = view.findViewById(R.id.textViewFechaEntrega);
                                    TextView textViewEstado = view.findViewById(R.id.textViewEstado);
                                    TextView textViewPrecio = view.findViewById(R.id.textViewPrecio);
                                    TextView textViewComentarios = view.findViewById(R.id.textViewComentarios);

                                    //Cargar los datos en los campos

                                    textViewFechaPedido.setText(String.valueOf(pedidoActivo.getFecha_pedido()));
                                    textViewFechaEntrega.setText(String.valueOf (pedidoActivo.getFecha_entrega()));
                                    textViewEstado.setText(String.valueOf(pedidoActivo.getEstado()));
                                    textViewPrecio.setText(String.valueOf(pedidoActivo.getPrecio_total() + "€"));
                                    textViewComentarios.setText(getString(R.string.comentarios) + String.valueOf(pedidoActivo.getComentarios()));
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError) {
                        Log.e("Error obtener pedidos", "Error al obtener los pedidos:", databaseError.toException());
                    }
                });
    }

    /**
     * Volver PPrincipal
     */
    private void volverPprincipal() {
        Intent intent = new Intent(VerPedidoActivity.this, PantallaPrincipalActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        volverPprincipal();
    }

}