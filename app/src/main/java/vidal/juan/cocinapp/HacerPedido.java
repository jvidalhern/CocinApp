package vidal.juan.cocinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HacerPedido extends AppCompatActivity {

    private Button cancelNuevopedidoButton;
    private Button seleccionarFechaEntregaButton;
    static final int apartirDiasRecoger = 2;//Dias definidos por esther ? sacar este dato de BBDD?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hacer_pedido);
        //referencia a items xml
        cancelNuevopedidoButton = findViewById(R.id.cancelNuevopedidoButton);
        seleccionarFechaEntregaButton = findViewById(R.id.seleccionarFechaEntregaButton);
        //Seleccionar la fecha
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
                volverPprincipal();
            }
        });

    }

    /**
     * Metodo para obener la fecha seleccionada en el datepicker
     */
    private void obtenerFechaDatepicker() {

        //Obtener referencias al dia de hoy
        Calendar calendar =  Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                //Showing the picked value in the textView
                //textView.setText(String.valueOf(year)+ "."+String.valueOf(month)+ "."+String.valueOf(day));

                // Formatear la fecha al formato deseado: aaaa-MM-dd
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                String fechaFormateada = formato.format(calendar.getTime());
                Toast.makeText(HacerPedido.this,"Fecha sel: " + fechaFormateada, Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(HacerPedido.this, PantallaPrincipal.class);
        startActivity(intent);
        finish();
    }
}