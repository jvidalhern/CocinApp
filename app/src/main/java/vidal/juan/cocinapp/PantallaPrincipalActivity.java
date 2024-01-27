package vidal.juan.cocinapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PantallaPrincipalActivity extends AppCompatActivity {
    //Items del xml
    TextView usuarioLogeadoTextView;
    Button editarRegistroButton, cerrarSesionButton,hacerPedido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        //Ref items del xml
        usuarioLogeadoTextView = findViewById(R.id.usuarioLogeadoTextView);
        editarRegistroButton = findViewById(R.id.editarRegistroButton);
        cerrarSesionButton = findViewById(R.id.cerrarSesionButton);
        hacerPedido = findViewById(R.id.hacerPedido);
        //FIREBASE; usuario logeado
        FirebaseUser usuarioLogeado = FirebaseAuth.getInstance().getCurrentUser();
        // Obtener el mail del usario logeado, todo Cambiar por el nombre de usuario con un bienvenidos?
        if (usuarioLogeado != null ){
            usuarioLogeadoTextView.setText(usuarioLogeado.getEmail());
        }

        //Accion del boton prueba para editar el registro
        editarRegistroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarRegistro();
            }
        });

        //Accion del boton nuevo pedido
        hacerPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hacerPedido();
            }
        });


        //TODO Accion del boton de prueba logout
        cerrarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            FirebaseAuth.getInstance().signOut();
            Intent volverLoginIntent = new Intent(PantallaPrincipalActivity.this, MainActivity.class);
            startActivity(volverLoginIntent);
            finish();
            }
        });



    }

    /**
     * Método para pasar a la ventana hacer pedido
     */
    private void hacerPedido() {
        Intent hacerPedidoIntent = new Intent(PantallaPrincipalActivity.this, SeleccionarRacionesActivity.class);
        startActivity(hacerPedidoIntent);
        finish();
    }

    /**
     * Meétodo para ir a la ventana de editar registro
     */
    private void editarRegistro() {
        //Pasar a la vista de editar registro
        Intent editarRegistroIntent   = new Intent(PantallaPrincipalActivity.this, EditarRegistroActivity.class);
        startActivity(editarRegistroIntent);
        finish();
    }
}