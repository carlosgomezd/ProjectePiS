package com.example.projectepis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPass extends AppCompatActivity {
    private EditText etEmail;
    private Button btreset;
    private String email="";

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        etEmail=(EditText) findViewById(R.id.et_Email);
        btreset=(Button) findViewById(R.id.btResetPass);

        mAuth= FirebaseAuth.getInstance();

        mDialog = new ProgressDialog( this);


        btreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = etEmail.getText().toString();

                if(!email.isEmpty()){
                    mDialog.setMessage("Espera un momento....");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    resetPassword();
                }else{
                    Toast.makeText(ResetPass.this, "Debes ingresar un email", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void resetPassword() {

        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ResetPass.this, "Se a enviado un correo para restablecer la contraseña", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ResetPass.this, "No se ha podido enviar un correo", Toast.LENGTH_SHORT).show();
                }

                mDialog.dismiss();

            }
        });

    }
}