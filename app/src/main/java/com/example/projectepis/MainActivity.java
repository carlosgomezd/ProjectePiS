package com.example.projectepis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    Button registrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registrar= (Button) findViewById(R.id.Registrar);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent registrar = new Intent(MainActivity.this,Registro.class);
                startActivity(registrar);

            }
        });



    }
}