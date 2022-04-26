package com.example.projectepis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    Button registrar;
    Button signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signIn = (Button) findViewById(R.id.Iniciar);
        registrar= (Button) findViewById(R.id.Registrar);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent registrar = new Intent(MainActivity.this,Registro.class);
                startActivity(registrar);

            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signIn = new Intent(MainActivity.this, InicioChatActivity.class);
                startActivity(signIn);

            }
        });



    }
}