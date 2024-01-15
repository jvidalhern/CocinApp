package vidal.juan.cocinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PantallaPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        //Items de la vista
        Button editarRegistroButton;
        editarRegistroButton = findViewById(R.id.editarRegistroButton);
        //Accion del boton prueba
        editarRegistroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarRegistro();
            }
        });

    }

    private void editarRegistro() {
        //Pasar a la vista de editar registro
        Intent editarRegistro   = new Intent(PantallaPrincipal.this, EditarRegistroActivity.class);
        startActivity(editarRegistro);
        finish();
    }
}