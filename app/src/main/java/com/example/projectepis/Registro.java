package com.example.projectepis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class Registro extends AppCompatActivity {
    EditText etDate;
    DatePickerDialog.OnDateSetListener setListener;
    TextInputLayout genero;
    AutoCompleteTextView autoCompleteTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        genero = findViewById(R.id.Genero);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        etDate = findViewById(R.id.et_date);


        //Crear array de genero
        String [] generos = new String[]{
                "Masculino",
                "Femenino",
                "Otros"
        };
        //Crear arrayadapter y configurarlo
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                Registro.this,
                R.layout.dropdown_genero,
                generos
        );

        //Setear adapter al autocomplete
        autoCompleteTextView.setAdapter(adapter);

        //Creamos el calendario el cual hacer doble click se muestra
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Registro.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month=month+1;
                        String date = day+"/"+month+"/"+year;
                        etDate.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });




    }
}