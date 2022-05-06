package com.example.projectepis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText etEmail,etPassword;
    private String email="";
    private String password="";
    private Button registrar,signIn,resetP;
    private DatabaseReference UserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signIn = (Button) findViewById(R.id.Iniciar);
        registrar= (Button) findViewById(R.id.Registrar);
        resetP= (Button) findViewById(R.id.btRecuperacion);

        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if(user != null){
            irinicio();
        }

        etEmail = (EditText) findViewById(R.id.TextEmail);
        etPassword = (EditText) findViewById(R.id.TextPassword);

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
                email=etEmail.getText().toString();
                password=etPassword.getText().toString();
                if(email.isEmpty() || password.isEmpty()){
                    email="*****";
                    password="*****";
                }
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            irinicio();
                        }else{
                            Toast.makeText(MainActivity.this, "Email o password incorrecto", Toast.LENGTH_SHORT ).show();
                        }
                    }
                });
            }
        });

        resetP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent respass = new Intent(MainActivity.this,ResetPass.class);
                startActivity(respass);
            }
        });

    }
    private void irinicio() {
        Intent i = new Intent(MainActivity.this,Inicio.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(i);
    }
}