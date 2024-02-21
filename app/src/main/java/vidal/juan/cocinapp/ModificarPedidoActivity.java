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
import android.widget.Toast;

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
    private double precioTotalPedido = 0;

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


                fechaPedidoDetalleTextMod.setText(pedido.getFecha_pedido().toString());
                fechaEntregaModTextview.setText( pedido.getFecha_entrega().toString());
                cometariosDetalleTextMod.setText(pedido.getComentarios().toString());
                totalDetalleTextMod.setText(String.valueOf(pedido.getPrecio_total()) + "\u20AC" );
                idPedidoModTextView.setText(getString(R.string.idPedidoString) + idPedido.substring(3,7));
                //Pasar el pedido a lista
                llenarLista(pedido);
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
                    TextView precioRacionDetalleVistaDetalle = view.findViewById(R.id.precioRacionDetalleVistaDetalle);
                    Button modDetalleBotonQuitar,modDetalleBotonAnadir;
                    modDetalleBotonQuitar = view.findViewById(R.id.modDetalleBotonQuitar);
                    modDetalleBotonAnadir = view.findViewById(R.id.modDetalleBotonAnadir);
                    //Precio racion
                    final double[] precioRacionActu = {0};
                    //Valores inciales
                    nombreRacionDetalle.setText(detallePedido.getRacion());
                    cantidadRacionDetalleVistaDetalle.setText(String.valueOf(detallePedido.getCantidad()));
                    precioRacionDetalleVistaDetalle.setText(String.valueOf (detallePedido.getPrecio()) + "\u20AC");
                    //Obtener datso de la racion por nombre de la racion para saber el max y el stock limitantes en la modificación
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("raciones").child(detallePedido.getRacion());
                    Log.d("Racion nombre", "Datos racion buscada : " + detallePedido.getRacion());
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Almacenar el total del pedido

                            //Obj Racion encontrado a partir del nombre de la racion
                            Racion racion = dataSnapshot.getValue(Racion.class);
                            Log.d("RacionEncontrada", "Racion encontrada por id: " + racion.toString());
                            // Configura los listeners para los botones
                            //Boton añadir
                            modDetalleBotonAnadir.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int cantidadActual = detallePedido.getCantidad();
                                    int cantidadMaxima = racion.getPedido_max();
                                    int stock = Integer.parseInt(racion.getStock());

                                    if (cantidadActual < Math.min(cantidadMaxima, stock)) {
                                        detallePedido.setCantidad(cantidadActual + 1);//Aumentar la cantidad en 1 del pedido
                                        cantidadRacionDetalleVistaDetalle.setText(String.valueOf(detallePedido.getCantidad()));//Mostrar la actu en la view
                                        double nuevoPrecio = Double.parseDouble(racion.getPrecio())*(detallePedido.getCantidad());
                                        detallePedido.setPrecio(nuevoPrecio);//Actualizar el precio del pedido
                                        precioRacionActu[0] = nuevoPrecio;
                                        precioRacionDetalleVistaDetalle.setText(String.valueOf (detallePedido.getPrecio()) + "\u20AC");//Mostrar actu del precio en la view
                                        precioTotalPedido += precioRacionActu[0];
                                        totalDetalleTextMod.setText(String.format("%.2f", precioTotalPedido) + "\u20AC");

                                    } else {
                                        if (cantidadActual == cantidadMaxima) {
                                            Toast.makeText(ModificarPedidoActivity.this, "Se ha alcanzado el máximo de productos de este tipo por pedido", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(ModificarPedidoActivity.this, "Se ha alcanzado el límite de productos disponibles en stock", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }
                            });
                            // Boton quitar
                            modDetalleBotonQuitar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (detallePedido.getCantidad() > 0) {
                                        detallePedido.setCantidad(detallePedido.getCantidad() - 1);
                                        if (detallePedido.getCantidad() == 0){
                                            Toast.makeText(ModificarPedidoActivity.this, detallePedido.getRacion() + " se eliminará del pedido", Toast.LENGTH_SHORT).show();
                                        }
                                        cantidadRacionDetalleVistaDetalle.setText(String.valueOf(detallePedido.getCantidad()));//Mostrar la actu en la view
                                        detallePedido.setPrecio(Double.parseDouble(racion.getPrecio())*(detallePedido.getCantidad()));//Actualizar el precio del pedido
                                        precioRacionDetalleVistaDetalle.setText(String.valueOf (detallePedido.getPrecio()) + "\u20AC");//Mostrar actu del precio en la view
                                        precioRacionActu[0] = Double.parseDouble(racion.getPrecio())*(detallePedido.getCantidad());
                                        precioTotalPedido -= precioRacionActu[0];
                                        totalDetalleTextMod.setText(String.format("%.2f", precioTotalPedido) + "\u20AC");
                                    }
                                }
                            });

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Manejar errores de la base de datos
                            Log.e("Error BBDD Racion", "Error al buscar la racion: " + databaseError.getMessage());
                        }
                        //Catidad max de la ración
                        //Stock atual de la ración
                    });
                }
            }
        });

    }
/*
    private void recalcularTotalPedido(Pedido pedido)
    {
        for (DetallePedidoNoParcel detalle : listaDetalles) {
            // Hacer algo con el detalle actual
        }
    }*/

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