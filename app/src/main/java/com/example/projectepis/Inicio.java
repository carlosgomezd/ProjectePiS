package com.example.projectepis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Inicio extends AppCompatActivity {
    Button cerrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        cerrar = findViewById(R.id.button);

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FirebaseAuth.getInstance().signOut();
                //Toast.makeText(Inicio.this, "Sesion cerrada", Toast.LENGTH_SHORT ).show();
                //gomain();
                Intent intent = new Intent(Inicio.this, InicioChatActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void gomain() {
        Intent i = new Intent(Inicio.this, InicioChatActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(i);
        finish();
    }

}