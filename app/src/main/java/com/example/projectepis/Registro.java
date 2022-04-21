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
    TextInputLayout genero;
    AutoCompleteTextView autoCompleteTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        genero = findViewById(R.id.Genero);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

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



    }
}