package vidal.juan.cocinapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.service.autofill.UserData;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Modificar el registro del usuario; una vez este ya esté logeado
 */
public class EditarRegistroActivity extends AppCompatActivity {
    private EditText modNombreEditText,modApellidoEditText,modDepartamentoEditText,modTelefonoEditText;
    //TODO: Para conectar a la BBDD hay que tener una rereferencia
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("usuarios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editarregistro);
        //Ref a los campos
        modNombreEditText = findViewById(R.id.modNombreEditText);
        modApellidoEditText = findViewById(R.id.modApellidoEditText);
        modDepartamentoEditText = findViewById(R.id.modDepartamentoEditText);
        modTelefonoEditText = findViewById(R.id.modTelefonoEditText);



        //rellenar el hint con los datos que ya tiene el usuario
        //obtenerlo de la bbdd los datos del usuario logeado; Ctemp: crear usuario temporal ?
        //Obtener cambios en los datos del usuario

        ValueEventListener datosUsuarioListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // obtener objeto con los datos del usuario
                //UserData user = dataSnapshot.getValue(UserData.class);
                //TODO: Investigar como filtrar los datos, la conexion con la BBDD ussuario la hace ahora hay que extraer los datos

                // ..
                //Resultados de la consulta
                String nombreUser = " ";
                //modNombreEditText.setText(dataSnapshot.toString());
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren())
                {
                    Usuarios usuarioDatos = userSnapshot.getValue(Usuarios.class);
                   // nombreUser = usuarioDatos.getNombre().toString();
                    //nombreUser = usuarioDatos.toString();
                    System.out.println("preuba" + usuarioDatos.toString());
                    Log.d("prueba",usuarioDatos.toString());


                }
               // modNombreEditText.setText(nombreUser);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // fallo al obtener los datos
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        myRef.addValueEventListener(datosUsuarioListener);

    }

}
