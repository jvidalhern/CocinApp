package vidal.juan.cocinapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistroActivity extends AppCompatActivity {

    private EditText nombreEditText;
    private EditText apellidosEditText;
    private EditText departamentoEditText;
    private EditText telefonoEditText;
    private EditText emailEditText;
    private EditText contrasenaEditText;
    private EditText repetirContrasenaEditText;
    private Button registroButton;

    private AlertDialog alertDialog;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://cocinaapp-7da53-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference("usuarios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Obtener referencias de los elementos en tu diseño
        nombreEditText = findViewById(R.id.nombreEditText);
        apellidosEditText = findViewById(R.id.apellidosEditText);
        departamentoEditText = findViewById(R.id.departamentoEditText);
        telefonoEditText = findViewById(R.id.telefonoEditText);
        emailEditText = findViewById(R.id.emailEditText);
        contrasenaEditText = findViewById(R.id.contrasenaEditText);
        repetirContrasenaEditText = findViewById(R.id.repetirContrasenaEditText);
        registroButton = findViewById(R.id.registroButton);

        // Configurar el listener del botón de registro
        registroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Realizar validaciones antes de procesar el registro
                if (validarNombre() && validarApellidos() && validarDepartamento() && validarTelefono() &&
                        validarEmail() && validarContrasena()) {

                    // Obtener el valor del correo electrónico
                    String email = emailEditText.getText().toString().trim();

                    // Verificar si el correo ya existe en la base de datos
                    verificarCorreoExistente(email);
                }
            }
        });

    }

    private void verificarCorreoExistente(final String email) {
        myRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // El correo ya está registrado
                    mostrarError("El correo electrónico ya está registrado. Intente con otro.");
                } else {
                    // El correo no existe, proceder con el registro
                    realizarRegistro();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", "Error en la consulta a la base de datos: " + databaseError.getMessage());
            }
        });
    }

    private void realizarRegistro() {
        // Obtener los valores de los campos del formulario
        String nombre = nombreEditText.getText().toString().trim();
        String apellidos = apellidosEditText.getText().toString().trim();
        String departamento = departamentoEditText.getText().toString().trim();
        String telefono = telefonoEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        // Crear un objeto Usuario con la información del formulario
        Usuarios nuevoUsuario = new Usuarios(nombre, apellidos, email, departamento, telefono);

        // Realizar la inserción en la base de datos
        insertarUsuarioEnBaseDeDatos(nuevoUsuario);
    }

    private void insertarUsuarioEnBaseDeDatos(Usuarios nuevoUsuario) {
        // Utilizar la referencia de la base de datos para insertar el nuevo usuario
        myRef.push().setValue(nuevoUsuario);
        Log.d("Usuarios", "Nuevo usuario: " + nuevoUsuario);

        // Mostrar un mensaje de éxito
        Toast.makeText(RegistroActivity.this, "Se ha enviado un email de confirmación, por favor verifique su correo.", Toast.LENGTH_SHORT).show();
        // Redirigir a la pantalla de inicio de sesión
        Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Cierra la actividad actual para que el usuario no pueda volver atrás
    }

    private boolean validarNombre() {
        String nombreApellido = nombreEditText.getText().toString().trim();
        // Patrón que permite letras, espacios y acentos
        String patron = "^[\\p{L}]+( [\\p{L}]+)*$";
        if (TextUtils.isEmpty(nombreApellido) || !nombreApellido.matches(patron)) {
            mostrarError("Introduzca un nombre válido");
            return false;
        }
        return true;
    }

    private boolean validarApellidos() {
        String nombreApellido = apellidosEditText.getText().toString().trim();
        // Patrón que permite letras, espacios y acentos
        String patron = "^[\\p{L}]+( [\\p{L}]+)*$";
        if (TextUtils.isEmpty(nombreApellido) || !nombreApellido.matches(patron)) {
            mostrarError("Introduzca unos apellidos válidos");
            return false;
        }
        return true;
    }

    private boolean validarDepartamento() {
        String departamento = departamentoEditText.getText().toString().trim();
        // Patrón que permite letras, espacios y acentos
        String patron = "^[\\p{L}]+( [\\p{L}]+)*$";
        if (TextUtils.isEmpty(departamento) || !departamento.matches(patron)) {
            mostrarError("Introduzca un departamento válido");
            return false;
        }
        return true;
    }

    private boolean validarTelefono() {
        String telefono = telefonoEditText.getText().toString().trim();

        // Verificar si el teléfono tiene exactamente 9 caracteres
        if (TextUtils.isEmpty(telefono) || telefono.length() != 9) {
            mostrarError("Introduzca un número de teléfono válido");
            return false;
        }

        // Patrón que permite solo números
        String patron = "^[0-9]+$";
        if (!telefono.matches(patron)) {
            mostrarError("Introduzca un número de teléfono válido");
            return false;
        }

        return true;
    }

    private boolean validarEmail() {
        String email = emailEditText.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mostrarError("Introduzca un correo electrónico válido");
            return false;
        }
        return true;
    }

    private boolean validarContrasena() {
        String contrasena = contrasenaEditText.getText().toString();
        String repetirContrasena = repetirContrasenaEditText.getText().toString();

        // Patrón que requiere al menos 8 caracteres, una mayúscula, una minúscula y un número
        String patron = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";

        if (TextUtils.isEmpty(contrasena) || !contrasena.matches(patron)) {
            mostrarError("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número");
            return false;
        }

        if (!contrasena.equals(repetirContrasena)) {
            mostrarError("Las contraseñas no coinciden");
            return false;
        }

        return true;
    }

    private void mostrarError(String mensaje) {
        // Inflar el diseño personalizado
        View customView = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);

        // Configurar el mensaje de error
        TextView errorMessageTextView = customView.findViewById(R.id.errorMessageTextView);
        errorMessageTextView.setText(mensaje);

        // Configurar el botón de OK
        Button okButton = customView.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra el AlertDialog al hacer clic en OK
                alertDialog.dismiss();
            }
        });

        // Crear un AlertDialog personalizado
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customView);

        // Configurar esquinas redondeadas
        alertDialog = builder.create(); // Actualizar la variable de clase
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_background);

        // Mostrar el AlertDialog
        alertDialog.show();
    }
}
