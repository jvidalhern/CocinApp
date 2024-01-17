package vidal.juan.cocinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PantallaPrincipal extends AppCompatActivity {
    //Items del xml
    TextView usuarioLogeadoTextView;
    Button editarRegistroButton, cerrarSesionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        //Ref items del xml
        usuarioLogeadoTextView = findViewById(R.id.usuarioLogeadoTextView);
        editarRegistroButton = findViewById(R.id.editarRegistroButton);
        cerrarSesionButton = findViewById(R.id.cerrarSesionButton);
        //TODO Accion del boton prueba para editar el registro
        editarRegistroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarRegistro();
            }
        });

        //TODO pruba de Datos del usuario logeado; dejar arriba en un menú mejor?
        FirebaseUser usuarioLogeado = FirebaseAuth.getInstance().getCurrentUser();
        //TODO Si ya esta logeado obtener el mail y probarlo; probar a conseguir el nombre del usuairo de la tabla usuarios a partir del correo
        if (usuarioLogeado != null ){
            usuarioLogeadoTextView.setText(usuarioLogeado.getEmail());
        }
        //TODO Accion del boton de prueba logout
        cerrarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            FirebaseAuth.getInstance().signOut();
            Intent volverLoginIntent = new Intent(PantallaPrincipal.this, MainActivity.class);
            startActivity(volverLoginIntent);
            finish();
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