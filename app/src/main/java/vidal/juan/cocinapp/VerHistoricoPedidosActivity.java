package vidal.juan.cocinapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
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
import java.util.Locale;

public class VerHistoricoPedidosActivity extends AppCompatActivity {

    private Button cancelVerPedidosActivosButton;
    private ListView listaPedidosActivos;
    private FirebaseUser usuarioLogeado;
    private AdaptadorPedidosActivos adaptadorPedidosActivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_historico);
        cancelVerPedidosActivosButton = findViewById(R.id.cancelVerPedidosActivosButton);
        listaPedidosActivos = findViewById(R.id.listaPedidosActivos);

        usuarioLogeado = FirebaseAuth.getInstance().getCurrentUser();

        cancelVerPedidosActivosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volverPrincipal();
            }
        });

        adaptadorPedidosActivos = new AdaptadorPedidosActivos(VerHistoricoPedidosActivity.this, R.layout.pedidos_historicos_vista) {
            @Override
            public void onEntrada(Pedido pedidoActivo, View view) {
                // Referencias a los elementos de la vista
                TextView textViewFechaPedido = view.findViewById(R.id.textViewFechaPedido);
                TextView textViewFechaEntrega = view.findViewById(R.id.textViewFechaEntrega);
                TextView textViewResumen = view.findViewById(R.id.textViewResumen);
                TextView textViewPrecio = view.findViewById(R.id.textViewPrecio);
                TextView textViewComentarios = view.findViewById(R.id.textViewComentarios);
                TextView textViewIdPedido = view.findViewById(R.id.textViewIdPedido);
                TableRow filaPedidoColor = view.findViewById(R.id.filaPedidoColor);
                LinearLayout linarLayoutDetallePedido = view.findViewById(R.id.linarLayoutDetallePedido);

                // Construir el resumen de las raciones
                StringBuilder resumenBuilder = new StringBuilder();

                for (DetallePedidoNoParcel detalle : pedidoActivo.getDetalles()) {
                    String cantidad = String.valueOf(detalle.getCantidad());
                    String nombreRacion = detalle.getRacion();
                    if (resumenBuilder.length() > 0) {
                        // Agregar espacios en blanco para alinear las líneas
                        resumenBuilder.append("\n");
                        int cantidadCaracteresCantidad = cantidad.length();
                        int cantidadEspacios = 5 - cantidadCaracteresCantidad;
                        for (int i = 0; i < cantidadEspacios; i++) {
                            resumenBuilder.append("");
                        }
                    }

                    resumenBuilder.append(cantidad)
                            .append("x")
                            .append(nombreRacion);
                }

                // Cargar los datos en los campos
                textViewIdPedido.setText(getString(R.string.id_pedido_string) + String.valueOf(pedidoActivo.getIdPedido()).substring(3, 7));
                String fechaPedido = String.valueOf(pedidoActivo.getFecha_pedido());
                // Reemplazar el primer espacio con un salto de línea
                fechaPedido = fechaPedido.replaceFirst(" ", "\n");
                // Asignar la cadena modificada al textViewFechaPedido
                textViewFechaPedido.setText(fechaPedido);
                //textViewFechaPedido.setText(String.valueOf(pedidoActivo.getFecha_pedido())); Para un solo intervalo de fecha
                String fechaEntrega = String.valueOf(pedidoActivo.getFecha_entrega());
                StringBuilder fechaFormateada = new StringBuilder();
                // Recorrer la cadena original y agregar un salto de línea antes de cada paréntesis abierto "("
                for (int i = 0; i < fechaEntrega.length(); i++) {
                    char currentChar = fechaEntrega.charAt(i);
                    // Agregar un salto de línea antes de cada paréntesis abierto "("
                    if (currentChar == '(' && i != 0) {
                        fechaFormateada.append("\n");
                    }
                    fechaFormateada.append(currentChar);
                }

                textViewFechaEntrega.setText(fechaFormateada.toString());

                textViewResumen.setText(resumenBuilder.toString());
                Locale locale = Locale.US;//Para poner el . como serparador
                textViewPrecio.setText(String.format(locale,"%.2f",pedidoActivo.getPrecio_total()) + "\u20AC");
                textViewComentarios.setText(getString(R.string.comentarios) +": " +  String.valueOf(pedidoActivo.getComentarios()));

                // Cambiar color fondo
                filaPedidoColor.setBackgroundColor(getResources().getColor(R.color.semiTransparente));
                textViewComentarios.setBackgroundColor(getResources().getColor(R.color.semiTransparente));

            }
        };

        listaPedidosActivos.setAdapter(adaptadorPedidosActivos);

        obtenerPedidos();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ActivityLifecycle", "onDestroy() Verpedido");
    }

    private void obtenerPedidos() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Log.d("database", "DatabaseReference" + databaseReference);

        databaseReference.child("pedidos").orderByChild("usuario").equalTo(usuarioLogeado.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Pedido> pedidosActivos = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Pedido pedido = snapshot.getValue(Pedido.class);
                            pedido.setIdPedido(snapshot.getKey());
                            if (pedido.getEstado().equals("recogido")) {
                                pedidosActivos.add(0, pedido);
                            }
                            // Actualizar la lista de pedidos en el adaptador
                            Log.d("VerPedidoActivity", "Atulizando lista");
                            adaptadorPedidosActivos.actualizarLista(pedidosActivos);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Error obtener pedidos", "Error al obtener los pedidos:", databaseError.toException());
                    }
                });
    }

    /**
     * Volver Pantalla principal
     */
    private void volverPrincipal() {
        finish();
    }

}
