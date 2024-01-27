package vidal.juan.cocinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HacerPedidoActivity extends AppCompatActivity {

    private Button cancelNuevopedidoButton;
    private Button seleccionarFechaEntregaButton;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private FirebaseUser usuarioLogeado ;
    String fechaEntrega;
    static final int apartirDiasRecoger = 5;//Dias definidos por esther ? sacar este dato de BBDD?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hacer_pedido);
        //referencia a items xml
        cancelNuevopedidoButton = findViewById(R.id.cancelNuevopedidoButton);
        seleccionarFechaEntregaButton = findViewById(R.id.seleccionarFechaEntregaButton);
        //Usuario logeado en la app
         usuarioLogeado = FirebaseAuth.getInstance().getCurrentUser();

        //Evento Seleccionar la fecha
        seleccionarFechaEntregaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFechaDatepicker();
            }
        });


        //Volver a la pantalla principal
        cancelNuevopedidoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HacerPedidoActivity.this,"La fecha de entrega seral: " + fechaEntrega, Toast.LENGTH_LONG).show();
                volverPprincipal();
            }
        });

    }

    /**
     * Metodo para obener la fecha seleccionada en el datepicker; Tiene restricion mediante la CONSTANTE a paritr dias recoge
     * Se muestran los dias siempre a partir de esta constante
     */
    private void obtenerFechaDatepicker() {

        //Obtener referencias al dia de hoy
        final Calendar calendar =  Calendar.getInstance();
        final int ano = calendar.get(Calendar.YEAR);
        final int mes = calendar.get(Calendar.MONTH);
        final int dia = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //Setear calendar con la fecha seleccionada
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                // Formatear la fecha al formato deseado: aaaa-MM-dd
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                String fechaFormateada = formato.format(calendar.getTime());
                //Que hacer cuando se seleccione la fecha
                Toast.makeText(HacerPedidoActivity.this,"Fecha sel: " + fechaFormateada, Toast.LENGTH_LONG).show();
                fechaEntrega = fechaFormateada;
            }
        }, ano, mes, dia);

        //Restriccion de fecha
        Calendar calendarioMin = Calendar.getInstance();
        //Apartir de los dias que diga ESTHER se puede recoger el pedido
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
     * /Método para insertar el pedido de un usuario una vez definido el objeto pedido a aprtir de la interfaz
     * @param nuevoPedido objeto pedido creado a paritr de los datos generados en la interfaz
     */
    private  void insertarPedidoParaUsuario( Pedido nuevoPedido) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            // ID para el pedido
            String nuevoPedidoId = databaseReference.child("pedidos").push().getKey();

            // ID de usuario al nuevo pedido
            nuevoPedido.setUsuarioId(usuarioLogeado.getUid());

            // Insertar el nuevo pedido en la colección de pedidos
            databaseReference.child("pedidos").child(nuevoPedidoId).setValue(nuevoPedido);

            //Insertar en indices para las busqeudas sencillas--TODO definir indices bien,
        }


}