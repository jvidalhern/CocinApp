package vidal.juan.cocinapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HacerPedidoActivity extends AppCompatActivity {

    private Button cancelNuevopedidoButton,seleccionarFechaEntregaButton,confirmarPedidoButton;
    private ListView listaDetalle;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private FirebaseUser usuarioLogeado ;
    private TextView total,fechaEntregaConfirm;
    private EditText comentariosTextMultiLine2;
    private String fechaEntrega, fechaPedido;
    private String comentarios = "Sin comentarios";
    private ArrayList<DetallePedido> detallesSeleccionados;
    private ArrayList<DetallePedidoNoParcel> detallesSeleccionadosNoParcel = new ArrayList<>();//Iincializar el array list para luego hacer la transformación
    private double precioTotal;
    // Formatear la fecha al formato deseado: aaaa-MM-dd
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
    static final int apartirDiasRecoger = 5;//Dias a partir de los cuales se puede recoger Dias definidos por esther ? TODO sacar este dato de BBDD?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hacer_pedido);
        //referencia a items xml
        cancelNuevopedidoButton = findViewById(R.id.cancelNuevopedidoButton);
        seleccionarFechaEntregaButton = findViewById(R.id.seleccionarFechaEntregaButton);
        confirmarPedidoButton = findViewById(R.id.confirmarPedidoButton);
        listaDetalle = findViewById(R.id.listaDetalle);
        total = findViewById(R.id.total);
        comentariosTextMultiLine2 = findViewById(R.id.comentariosTextMultiLine2);
        fechaEntregaConfirm =  findViewById(R.id.fechaEntregaConfirm);

        //Usuario logeado en la app
         usuarioLogeado = FirebaseAuth.getInstance().getCurrentUser();

        // Recuperar detalles seleccionados y precio total de la anterior actividad
        detallesSeleccionados = getIntent().getParcelableArrayListExtra("detallesSeleccionados");
        precioTotal = getIntent().getDoubleExtra("precioTotal", 0.0);

        //Prueba mostrar en el log lo que se ha seleccionado
        for (DetallePedido detalle : detallesSeleccionados) {
            Log.d("DetallesSeleccionadostoinsert", "Nombre: " + detalle.getRacion() +
                    ", Cantidad: " + detalle.getCantidad() +
                    ", Precio : " + detalle.getPrecio() + "precio total: " + precioTotal);
        }
        //Llenar la lista de la vista detalle_pedido_vista.xml  con los detalles obtenidos para mostrarlos como confirmación
        listaDetalle.setAdapter(new AdaptadorDetalles(HacerPedidoActivity.this, R.layout.detalle_pedido_vista, detallesSeleccionados) {
            @Override
            public void onEntrada(DetallePedido detallePedido, View view) {
                if (detallePedido != null) {
                    TextView nombreRacionDetalle = view.findViewById(R.id.nombreRacionDetalle);
                    TextView cantidadRacionDetalleVistaDetalle = view.findViewById(R.id.cantidadRacionDetalleVistaDetalle);
                    TextView cantidadRacionVistaDetalle = view.findViewById(R.id.cantidadRacionVistaDetalle);
                    TextView precioRacionDetalleVistaDetalle = view.findViewById(R.id.precioRacionDetalleVistaDetalle);

                    nombreRacionDetalle.setText(detallePedido.getRacion());
                    cantidadRacionDetalleVistaDetalle.setText(String.valueOf(detallePedido.getCantidad()));
                    precioRacionDetalleVistaDetalle.setText(String.valueOf (detallePedido.getPrecio()));


                }
            }
        });
        //Mostrar el total del pedido; obtenido de la activdad anterior
        total.setText("Total: " + String.valueOf(precioTotal));//Todo formatear mejor esto, redondearlo

        //Evento Botón Seleccionar fecha de entrga
        seleccionarFechaEntregaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Obtener la fecha seleccionada
                obtenerFechaDatepicker();
            }
        });

        //Evento para confirmar el pedido, se muestra una vez obtenida la fecha, y que se graba en la BBDD
        confirmarPedidoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Insertar en la BBDD el nuevo pedido a partir de los datos de la interfaz
                insertarPedidoParaUsuario();
            }
        });


        //Volver a la pantalla principal
        cancelNuevopedidoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HacerPedidoActivity.this,"Pedido cancelado" , Toast.LENGTH_LONG).show();
                volverPprincipal();
            }
        });

    }

    /**
     * Metodo para obener la fecha seleccionada en el datepicker; Tiene restricion mediante la CONSTANTE a paritr dias recoge
     * Se muestran los dias siempre a partir de esta constante. Tabién se obtiene la fecha del dia actual formateada
     */
    private void obtenerFechaDatepicker() {

        //Obtener referencias al dia de hoy
        final Calendar calendar =  Calendar.getInstance();
        final int ano = calendar.get(Calendar.YEAR);
        final int mes = calendar.get(Calendar.MONTH);
        final int dia = calendar.get(Calendar.DAY_OF_MONTH);
        //Formatear la fecha a dia de hoy para pasarla al insert
        fechaPedido = formato.format(calendar.getTime());
        DatePickerDialog datePickerDialog = new DatePickerDialog(this , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //Setear calendar con la fecha seleccionada
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);


                String fechaFormateada = formato.format(calendar.getTime());
                //Que hacer cuando se seleccione la fecha
                Toast.makeText(HacerPedidoActivity.this,"Fecha de entrega: " + fechaFormateada, Toast.LENGTH_LONG).show();
                fechaEntrega = fechaFormateada;
                Log.d("FechaSel", "FechaSel: " + fechaEntrega);
                Log.d("FechaPedido", "FechaPedido: " + fechaPedido);
                fechaEntregaConfirm.setText(fechaEntrega);
                confirmarPedidoButton.setVisibility(View.VISIBLE);
            }
        }, ano, mes, dia);

        //Restriccion de fecha
        Calendar calendarioMin = Calendar.getInstance();
        //Apartir de los dias que diga ESTHER se puede recoger el pedido todo esto se hace con una constante local; habria que hacerlo de BBDD para que sea mas sencillo de configurar
        calendarioMin.add(Calendar.DAY_OF_MONTH, + apartirDiasRecoger);
        //Setear el dia minimo
        datePickerDialog.getDatePicker().setMinDate(calendarioMin.getTimeInMillis() - 1000);
        //TODO hace falta setear dia máximo?
        datePickerDialog.show();


    }

    /**
     * Para volver a la ventana principal
     */
    private  void volverPprincipal() {
        Intent intent = new Intent(HacerPedidoActivity.this, PantallaPrincipalActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * /Método para insertar el pedido de un usuario
     *
     */
    private  void insertarPedidoParaUsuario( ) {

            //BBDD referencia para el insert
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            // ID para el pedido
            String nuevoPedidoId = databaseReference.child("pedidos").push().getKey();
            // ID de usuario para el pedido
            String userId = usuarioLogeado.getUid();
            //La fecha seleccionada y la fecha de pedido ya han sido obtenidas en la activity; fechaPedido y fechaEntrega
            //Los comentarios del pedido
            comentarios = comentariosTextMultiLine2.getText().toString();
            //Crear el pedido a partir de los datos seleecionados
            //Pasar los detalles a un objeto que no implemente parcelable para que no inserte stability 0 en firebase
            transFormNoParcel();
            //Crear el bojeto pedido con los datos
            Pedido nuevoPedido = new Pedido(comentarios,detallesSeleccionadosNoParcel,"PedidoApp",fechaPedido,fechaEntrega,precioTotal,userId);
            //Log para ver pedido
            Log.d("NuevoPedido", "Pedido: " + nuevoPedido.toString());
            // Insertar el nuevo pedido en la colección de pedidos verificando si ha ido bien o no
            databaseReference.child("pedidos").child(nuevoPedidoId).setValue(nuevoPedido).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // DATO MODIFICADO EN BBDD
                            Toast.makeText(HacerPedidoActivity.this,"Pedido registrado "  , Toast.LENGTH_LONG).show();
                            //volver a la ventana principal
                            volverPprincipal();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(HacerPedidoActivity.this,"ERROR No se ha registrado el pedido "  , Toast.LENGTH_LONG).show();
                        }
                    });;

            //Insertar en indices para las busqeudas sencillas--TODO definir indices bien,
        }

    /**
     * Método para transformar un detalle de pedido de la clase DetallePedido en un objeto exactamente igual de la clase DetallePedidoNoParcel pero sin que implemente la interfaz pacelable
     * Previamente usada para pasar los detalles entre activity, de no hacerlo se pasa el campo stability = 0 ya que firebase usa parcelable al insertar el objeto y no se puede ignorar pasarlo
     * La solución es crear un nuevo objeto exactamente igual sin implementar parcelable
     */
    private void transFormNoParcel() {
        for (DetallePedido detalle : detallesSeleccionados) {
            //Cada objeto de la lista detallesSeleccionados pasarlo al arraylist DetallePedidoNoParcel exactamente igual a como estaba
            detallesSeleccionadosNoParcel.add(new DetallePedidoNoParcel(detalle.getRacion(), detalle.getCantidad(), detalle.getPrecio()));
        }
        Log.d("Transformación", "detallesSeleccionadosNoParcel: " + detallesSeleccionadosNoParcel.toString());
    }
}