package com.example.projectepis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    EditText etDate;
    EditText etName;
    EditText etApellido;
    EditText etEmail;
    EditText etPassword;
    Button btRegistro;

    //Variables de datos que vamos a registrar
    String name="";
    String apellido="";
    String email="";
    String password="";

    //Autentificacion y base de datos
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

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

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Datos para Registrar y la Base de datos
        etName = (EditText) findViewById(R.id.et_Nombre);
        etApellido = (EditText) findViewById(R.id.et_Apellido);
        etEmail = (EditText) findViewById(R.id.et_Email);
        etPassword = (EditText) findViewById(R.id.et_Password);
        btRegistro = (Button) findViewById(R.id.bt_Registrar);

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

        //Introducir los datos y la comprobacion para el Registro de Usuarios

        btRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etName.getText().toString();
                apellido = etApellido.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                if(!name.isEmpty() && !apellido.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                   if(password.length()>=6){
                       registerUser();
                   }else{
                       Toast.makeText(Registro.this, "El password debe tener al menos 6 caracteres", Toast.LENGTH_SHORT ).show();
                   }
                }else{
                    Toast.makeText(Registro.this, "Debes completar los campos", Toast.LENGTH_SHORT ).show();
                }
            }
        });



    }
    //Registro y base de datos la cual guardara los valores de nuestros usuario en Firebase
    private void registerUser(){

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Map<String , Object> map = new HashMap<>();
                    map.put("nombre",name );
                    map.put("apellido",apellido );
                    map.put("email",email );
                    map.put("password",password );

                    String id = mAuth.getCurrentUser().getUid();


                    mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){

                                startActivity(new Intent(Registro.this,MainActivity.class));
                                finish();
                            }else{
                                Toast.makeText(Registro.this, "No se pudo crear los datos correctamente", Toast.LENGTH_SHORT ).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(Registro.this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT ).show();
                }
            }
        });

    }



}