package vidal.juan.cocinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VerPedidoActivity extends AppCompatActivity {

    private Button cancelVerPedidosActivosButton;
    private ListView listaPedidosActivos;
    private FirebaseUser usuarioLogeado ;
    //private ArrayList<Pedido> pedidosActivos = new ArrayList<>();//tODO QUITAR LISTA DE AQUI PARA HACER EL CAMBIO EN REALTIME
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
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {
                        ArrayList<Pedido> pedidosActivos = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Pedido pedido = snapshot.getValue(Pedido.class);
                            pedido.setIdPedido(snapshot.getKey());
                            // Verificar si el pedido tiene estado "preparar" o "recoger"
                            if (pedido != null && ("preparar".equals(pedido.getEstado()) || "recoger".equals(pedido.getEstado()))) {
                                pedidosActivos.add(pedido);
                                Log.d("PedidoACTIVOENCNTRADO", "Pedido encontrado por id: " + pedido.toString());
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
                                        TextView textViewIdPedido = view.findViewById(R.id.textViewIdPedido);
                                        TableRow filaPedidoColor = view.findViewById(R.id.filaPedidoColor);
                                        LinearLayout linarLayoutDetallePedido = view.findViewById(R.id.linarLayoutDetallePedido);


                                        //Cargar los datos en los campos

                                        textViewIdPedido.setText("ID pedido: " + String.valueOf(pedidoActivo.getIdPedido()).substring(3,7));
                                        textViewFechaPedido.setText(String.valueOf(pedidoActivo.getFecha_pedido()));
                                        textViewFechaEntrega.setText(String.valueOf (pedidoActivo.getFecha_entrega()));
                                        textViewEstado.setText(String.valueOf (pedidoActivo.getEstado()));
                                        textViewPrecio.setText(String.valueOf (pedidoActivo.getPrecio_total()) + "\u20AC");
                                        textViewComentarios.setText(getString(R.string.comentarios) + String.valueOf (pedidoActivo.getComentarios()) );

                                        //Evento de click en el pedido para pasar a los detalles del pedido
                                        linarLayoutDetallePedido.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                verDetallesDEPedido(pedidoActivo.getIdPedido());
                                            }
                                        });

                                        //Cambiar colo en funcion del estado
                                        if (pedidoActivo.getEstado().equals("preparar"))
                                        {
                                            filaPedidoColor.setBackgroundColor(getResources().getColor(R.color.prepararPedidoColor));
                                            textViewComentarios.setBackgroundColor(getResources().getColor(R.color.prepararPedidoColor));
                                        } else if (pedidoActivo.getEstado().equals("recoger")) {
                                                filaPedidoColor.setBackgroundColor(getResources().getColor(R.color.recogerPedidoColor));
                                                textViewComentarios.setBackgroundColor(getResources().getColor(R.color.recogerPedidoColor));
                                                }

                                                else{
                                                    filaPedidoColor.setBackgroundColor(getResources().getColor(R.color.defPedidoColor));
                                                    textViewComentarios.setBackgroundColor(getResources().getColor(R.color.defPedidoColor));}

                                    }
                                }
                            });
                        }
                        //Prueba en log
                        for (Pedido pedido : pedidosActivos) {
                            Log.d("Pedido", "Fecha Pedido: " + pedido.getFecha_pedido() +
                                    ", Fecha Entrega: " + pedido.getFecha_entrega() +
                                    ", Estado: " + pedido.getEstado() +
                                    ", Precio: " + pedido.getPrecio_total());
                        }

                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError) {
                        Log.e("Error obtener pedidos", "Error al obtener los pedidos:", databaseError.toException());
                    }
                });
    }

    /**
     * Método para pasar a la actividad de ver los detalles del peiddo clickado
     */
    private void verDetallesDEPedido(String idPedido) {
        Intent verDetallesPedido = new Intent(VerPedidoActivity.this, VerDetallesPedidoActivity.class);
        verDetallesPedido.putExtra("idPedido", idPedido);
        startActivity(verDetallesPedido);
        finish();
    }

    /**
     * Volver PPrincipal
     */
    private  void volverPprincipal() {
        Intent intent = new Intent(VerPedidoActivity.this, PantallaPrincipalActivity.class);
        startActivity(intent);
        finish();
    }

}