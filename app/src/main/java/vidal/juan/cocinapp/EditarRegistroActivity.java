package vidal.juan.cocinapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.service.autofill.UserData;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Modificar el registro del usuario; una vez este ya esté logeado
 */
public class EditarRegistroActivity extends AppCompatActivity {
    //Items de la vista
    private EditText modNombreEditText,modApellidoEditText,modDepartamentoEditText,modTelefonoEditText;

    private static Usuarios usuarioDatosOriginal;

    public Usuarios getUsuarioDatosOriginal() {
        return usuarioDatosOriginal;
    }

    public void setUsuarioDatosOriginal(Usuarios usuarioDatosOriginal) {
        this.usuarioDatosOriginal = usuarioDatosOriginal;
    }

    //Uusario logeado en la app
    FirebaseUser usuarioLogeado = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editarregistro);
        //Ref a los campos de la vista
        modNombreEditText = findViewById(R.id.modNombreEditText);
        modApellidoEditText = findViewById(R.id.modApellidoEditText);
        modDepartamentoEditText = findViewById(R.id.modDepartamentoEditText);
        modTelefonoEditText = findViewById(R.id.modTelefonoEditText);
        //Para conectar a la BBDD mediante una referencia
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://cocinaapp-7da53-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference().child("usuarios");
        //Consulta para conseguir los datos del usuario logedao;filtrar por el mail
        Query datosUsuarioLogeado = myRef.orderByChild("email").equalTo(usuarioLogeado.getEmail());
        //"Ejecutar la consulta"
        obtenerDatosUserLogeado(datosUsuarioLogeado);
        //Toast.makeText(EditarRegistroActivity.this,nombreUserOriginal, Toast.LENGTH_LONG).show();
        //Validar la modificacion de datos por parte del usuario
        //Log.d("pruebaaa","origi " + nombreUserOriginal);
        //Log.d("pruebaaa","debbd " + modNombreEditText.getText().toString());
        //verificarModificacionCampo(modNombreEditText,nombreUserOriginal);

    }

    /**
     * Método para obtener los datos personales del usuario logeado
     * @param datosUsuarioLogeado Query con referencia al email del usuario logeado
     */
    public void obtenerDatosUserLogeado(Query datosUsuarioLogeado) {
        final Usuarios[] user = {new Usuarios()};
        datosUsuarioLogeado.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //El resultado se devuelve en un DataSnapshot, hay que recorrer con un FOR aunq solo sea un resultado
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren())
                {
                    //Crear un objeto usuario a partir de los datos filtrados para el correo del usuario logeado
                      Usuarios usuarioDatosAux = userSnapshot.getValue(Usuarios.class);
                    //Obtener los datos para mostrarlos en la vista
                    String nombreUserOriginal = usuarioDatosAux.getNombre();
                    String apellidosOriginal = usuarioDatosAux.getApellidos();
                    String departamentoOriginal = usuarioDatosAux.getDepartamento();
                    String telefonoOriginal = usuarioDatosAux.getTelefono();
                    setUsuarioDatosOriginal( new Usuarios(nombreUserOriginal,apellidosOriginal,usuarioDatosAux.getEmail(),usuarioDatosAux.getDepartamento(),telefonoOriginal));
                    //Pasar los datos origianles a la vista
                    modNombreEditText.setText(nombreUserOriginal);
                    modApellidoEditText.setText(apellidosOriginal);
                    modDepartamentoEditText.setText(departamentoOriginal);
                    modTelefonoEditText.setText(telefonoOriginal);
                    //Toast.makeText(EditarRegistroActivity.this,user[0].getNombre(), Toast.LENGTH_LONG).show();
                }
                Toast.makeText(EditarRegistroActivity.this,usuarioDatosOriginal.getNombre(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // fallo al obtener los datos
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        //Toast.makeText(EditarRegistroActivity.this,usuarioDatosOriginal.getNombre(), Toast.LENGTH_LONG).show();
    }

    public void verificarModificacionCampo(EditText campoValidar, String campoOriginal)
    {
        if (!campoValidar.getText().equals(campoOriginal))
        {
            campoValidar.setError(null);
        }
        else campoValidar.setError("");
    }

}
