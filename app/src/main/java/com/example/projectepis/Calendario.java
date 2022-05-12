package com.example.projectepis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.CharSequenceTransformation;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Calendario extends AppCompatActivity implements CalendarView.OnDateChangeListener {
    private CalendarView calendarView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.calendario);

        toolbar=(Toolbar)findViewById(R.id.app_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Calendari");

        calendarView=(CalendarView) findViewById(R.id.calendarioView);
        calendarView.setOnDateChangeListener(this);

    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        CharSequence[] items = new CharSequence[3];
        items[0]="Agregar evento";
        items[1]="Ver Eventos";
        items[2]="Cancelar";
        final int dia,mes,anio;
        dia=i;
        mes=i1+1;
        anio=i2;


        builder.setTitle("Seleccione una tarea")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0){
                            //agregar eventos

                            //Intent intent = new Intent(getApplication(),agregarEventoCalendario.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("dia",dia);
                            bundle.putInt("mes",mes);
                            bundle.putInt("anio",anio);
                           // intent.putExtras(bundle);
                            //startActivity(intent);

                        }else if (i==1){
                            //ver eventos

                           // Intent intent = new Intent(getApplication(),verEventos.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("dia",dia);
                            bundle.putInt("mes",mes);
                            bundle.putInt("anio",anio);
                            //intent.putExtras(bundle);
                            //startActivity(intent);


                        }else{
                            return;
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();



    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.chat:
                    Intent intent = new Intent(Calendario.this, InicioChatActivity.class);
                    startActivity(intent);
                    break;
                case R.id.crear_evento:
                    Intent intent2 = new Intent(Calendario.this, CrearEvento.class);
                    startActivity(intent2);
                    break;
                case R.id.evento_destacado:
                    Intent intent3 = new Intent(Calendario.this, EventoDestacado.class);
                    startActivity(intent3);
                    break;
                case R.id.perfil:
                    //Intent intent4 = new Intent(Calendario.this, Perfil.class);
                    //startActivity(intent4);
                    break;
            }
            return true;
        }
    };


}