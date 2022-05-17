package com.example.projectepis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class Inicio extends AppCompatActivity {
    Timer cerrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inicio);

        cerrar = new Timer();
        cerrar.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Inicio.this, InicioChatActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

    };}
/*
    private void gomain() {
        Intent i = new Intent(Inicio.this, InicioChatActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(i);
        finish();
    }

}*/
