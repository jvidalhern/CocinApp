package vidal.juan.cocinapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //Reseteo de contraseña
    TextView olvidoContrasenaTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView registroTextView = findViewById(R.id.registroTextView);
        registroTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });
        //Pasar del main a pantalla de resestablecer psw
        TextView olvidoContrasenaTextView = findViewById(R.id.olvidoContrasenaTextView);
        olvidoContrasenaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReestablecerPswActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
