package com.example.projectepis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Inicio extends AppCompatActivity {
    Button cerrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        cerrar = findViewById(R.id.button);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inicio.this, InicioChatActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.calendario:
                    //Intent intent = new Intent(InicioChatActivity.this, CalendarioActivity.class);
                    //startActivity(intent);
                    break;
                case R.id.chat:
                    Intent intent = new Intent(Inicio.this, InicioChatActivity.class);
                    startActivity(intent);
                    break;
                case R.id.crear_evento:
                    //Intent intent = new Intent(InicioChatActivity.this, CalendarioActivity.class);
                    //startActivity(intent);
                    break;
                case R.id.evento_destacado:
                    //Intent intent = new Intent(InicioChatActivity.this, CalendarioActivity.class);
                    //startActivity(intent);
                    break;
                case R.id.perfil:

                    //Intent intent = new Intent(InicioChatActivity.this, CalendarioActivity.class);
                    //startActivity(intent);
                    break;
            }
            return true;
        }
    };



    private void gomain() {
        Intent i = new Intent(Inicio.this, InicioChatActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(i);
        finish();
    }

}