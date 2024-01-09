package vidal.juan.cocinapp;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditarRegistroActivity extends AppCompatActivity {
    private EditText modNombre,modDepartamento,modTelefono;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editarregistro);
        //rellenar el hint con los datos que ya tiene el usuario
        //obtenerlo de la bbdd
    }

}
