package vidal.juan.cocinapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DecimalFormatSymbols;

public class PantallaPrincipalActivity extends AppCompatActivity {
    //Items del xml
    TextView usuarioLogeadoTextView;
    Button hacerPedidoButton,verPedidosButton,verHistoricoButton,userButton;
    private FirebaseUser usuarioLogeado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);
        //Ref items del xml
        usuarioLogeadoTextView = findViewById(R.id.usuarioLogeadoTextView);
        hacerPedidoButton = findViewById(R.id.hacerPedidobutton);
        verPedidosButton = findViewById(R.id.verPedidosButton);
        verHistoricoButton = findViewById(R.id.verHistoricoButton);
        userButton = findViewById(R.id.userButton);
        //FIREBASE; usuario logeado
        usuarioLogeado = FirebaseAuth.getInstance().getCurrentUser();
        //Token del usuario logeado
        tokenAppp();
        //Accion del boton nuevo pedido
        hacerPedidoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hacerPedido();
            }
        });

        //Accion del boton ver pedidos
        verPedidosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verPedidos();
            }
        });
        //Acción del boton de perfil de usuario
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarOpcionesUsuario();
            }
        });

    }

    /**
     * Método para pasar a la ventana ver pedidos en estado preparar o recoger
     */
    private void verPedidos() {
        Intent verPedidosIntent = new Intent(PantallaPrincipalActivity.this, VerPedidoActivity.class);
        startActivity(verPedidosIntent);
    }

    /**
     * Método para pasar a la ventana hacer pedido
     */
    private void hacerPedido() {
        Intent hacerPedidoIntent = new Intent(PantallaPrincipalActivity.this, SeleccionarRacionesActivity.class);
        startActivity(hacerPedidoIntent);
    }

    /**
     * Meétodo para ir a la ventana de editar registro
     */
    private void editarRegistro() {
        //Pasar a la vista de editar registro
        Intent editarRegistroIntent   = new Intent(PantallaPrincipalActivity.this, EditarRegistroActivity.class);
        startActivity(editarRegistroIntent);
    }

    /**
     * Método para cerrar la sesión
     */
    private void cerrarSesion(){
        FirebaseAuth.getInstance().signOut();
        Intent volverLoginIntent = new Intent(PantallaPrincipalActivity.this, MainActivity.class);
        startActivity(volverLoginIntent);
        finish();
    }

    /**
     * Recuperar el token unico de la aplicacion instalada en el dispositivo y asignarlo al usuario para poder enviar las notificaciones
     */
    private void tokenAppp (){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("ErrorTokenNotificacion", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        // referencia de la base de datos
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        if (usuarioLogeado != null){
                            String userLog = usuarioLogeado.getUid();
                            DatabaseReference usuariosRef = databaseReference.child("usuarios").child(userLog);
                            // token del usuario a BBDD
                            usuariosRef.child("tokenNoti").setValue(token);
                        }
                    }
                });
    }

    /**
     * Metodo para mostrar un dialog con las opciones del perfil de usuario.
     */
    private void mostrarOpcionesUsuario() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_opciones_usuario);
        //Botones del dialog
        Button editarRegistroButton = dialog.findViewById(R.id.editarRegistroButton);
        Button cerrarSesionButton = dialog.findViewById(R.id.cerrarSesionButton);
        Button eliminarCuentaButton = dialog.findViewById(R.id.eliminarCuentaButton);
        Button volverButton = dialog.findViewById(R.id.volverButton);
        //Accion de los botones del diálog
        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        editarRegistroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarRegistro();
                dialog.dismiss();
            }
        });
        cerrarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
                dialog.dismiss();
            }
        });
        eliminarCuentaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}