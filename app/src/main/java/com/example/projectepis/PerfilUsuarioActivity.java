package com.example.projectepis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class PerfilUsuarioActivity extends AppCompatActivity {

    private String usuario_recibido, CurrentEstado, CurrentUserId;
    private TextView usuarioNombre, usuarioApellido;
    private ImageView usuario_imagen;
    private Button enviarMensaje, cancelarMensaje;
    private FirebaseAuth auth;
    private DatabaseReference UserRef, SolicitudRef, ContactosRef, NotificacionesRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        usuario_recibido = getIntent().getExtras().get("usuario_id").toString();

        ContactosRef = FirebaseDatabase.getInstance().getReference().child("Contactos");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        SolicitudRef = FirebaseDatabase.getInstance().getReference().child("Solicitudes");
        NotificacionesRef = FirebaseDatabase.getInstance().getReference().child("Notificaciones");
        auth = FirebaseAuth.getInstance();
        CurrentUserId = auth.getCurrentUser().getUid();
        usuarioNombre = (TextView) findViewById(R.id.usuario_vic_nombre);
        usuarioApellido = (TextView) findViewById(R.id.usuario_vic_apellido);
        usuario_imagen = (ImageView) findViewById(R.id.usuario_vic_imagen);
        enviarMensaje = (Button) findViewById(R.id.enviar_mensaje_usuario_vic);
        cancelarMensaje = (Button) findViewById(R.id.cancelar_mensaje_usuario_vic);
        CurrentEstado = "nuevo";

        ObtenerInformacionDB();
    }

    private void ObtenerInformacionDB() {
        UserRef.child(usuario_recibido).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if((snapshot.exists())&& (snapshot.hasChild("imagen"))){
                    String nombreUser =snapshot.child("nombre").getValue().toString();
                    String apellidoUser =snapshot.child("apellido").getValue().toString();
                    String imagenUser =snapshot.child("imagen").getValue().toString();
                    Picasso.get().load(imagenUser).placeholder(R.drawable.user).into(usuario_imagen);
                    usuarioNombre.setText(nombreUser);
                    usuarioApellido.setText(apellidoUser);
                    MotorEnviarSolicitud();
                }else{
                    String nombreUser = snapshot.child("nombre").getValue().toString();
                    String apellidoUser =snapshot.child("apellido").getValue().toString();
                    usuarioNombre.setText(nombreUser);
                    usuarioApellido.setText(apellidoUser);
                    MotorEnviarSolicitud();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}});
    }

    private void MotorEnviarSolicitud() {

        SolicitudRef.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(usuario_recibido)){
                    String requerimiento = snapshot.child(usuario_recibido).child("tipo").getValue().toString();
                    if(requerimiento.equals("enviado")){
                        CurrentEstado = "enviado";
                        enviarMensaje.setText("CANCELAR SOLICITUD");
                    }else if(requerimiento.equals("recibido")){
                        CurrentEstado = "recibido";
                        enviarMensaje.setText("ACEPTAR SOLICITUD");

                        cancelarMensaje.setVisibility(View.VISIBLE);
                        cancelarMensaje.setEnabled(true);
                        cancelarMensaje.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CancelarSolicitud();
                            }
                        });
                    }
                }else{
                    ContactosRef.child(CurrentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(usuario_recibido)){
                                CurrentEstado="amigos";
                                enviarMensaje.setText("ELIMINAR CONTACTO");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(!CurrentUserId.equals(usuario_recibido)){
            enviarMensaje.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enviarMensaje.setEnabled(false);
                    if(CurrentEstado.equals("nuevo")){
                        EnviarSolicitud();
                    }
                    if (CurrentEstado.equals("enviado")){
                        CancelarSolicitud();
                    }
                    if (CurrentEstado.equals("recibido")){
                        AceptarSolicitud();
                    }
                    if (CurrentEstado.equals("amigos")){
                        EliminarContacto();
                    }
                }
            });
        }else{
            enviarMensaje.setVisibility(View.GONE);
        }
    }

    private void EliminarContacto() {
        ContactosRef.child(CurrentUserId).child(usuario_recibido).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            ContactosRef.child(usuario_recibido).child(CurrentUserId)
                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        enviarMensaje.setEnabled(true);
                                        CurrentEstado="nuevo";
                                        enviarMensaje.setText("ENVIAR SOLICITUD");

                                        cancelarMensaje.setVisibility(View.GONE);
                                        cancelarMensaje.setEnabled(false);
                                    }
                                }
                            });
                        }
                    }
                });
    }

    private void AceptarSolicitud() {
        ContactosRef.child(CurrentUserId).child(usuario_recibido).child("Contacto").setValue("Aceptado")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            ContactosRef.child(usuario_recibido).child(CurrentUserId).child("Contacto")
                                    .setValue("Aceptado").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        SolicitudRef.child(CurrentUserId).child(usuario_recibido).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            SolicitudRef.child(usuario_recibido).child(CurrentUserId)
                                                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    enviarMensaje.setEnabled(true);
                                                                    CurrentEstado="amigos";
                                                                    enviarMensaje.setText("ELIMINAR CONTACTO");
                                                                    cancelarMensaje.setVisibility(View.GONE);
                                                                    cancelarMensaje.setEnabled(false);
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    }
                });
    }

    private void CancelarSolicitud() {
        SolicitudRef.child(CurrentUserId).child(usuario_recibido).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            SolicitudRef.child(usuario_recibido).child(CurrentUserId)
                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        enviarMensaje.setEnabled(true);
                                        CurrentEstado="nuevo";
                                        enviarMensaje.setText("ENVIAR SOLICITUD");

                                        cancelarMensaje.setVisibility(View.GONE);
                                        cancelarMensaje.setEnabled(false);
                                    }
                                }
                            });
                        }
                    }
                });
    }

    private void EnviarSolicitud() {
        SolicitudRef.child(CurrentUserId).child(usuario_recibido).child("tipo").setValue("enviado")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            SolicitudRef.child(usuario_recibido).child(CurrentUserId).child("tipo")
                                    .setValue("recibido").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        HashMap<String, String> chatNotificacion = new HashMap<>();
                                        chatNotificacion.put("de", usuario_recibido);
                                        chatNotificacion.put("tipo", "requerimiento");
                                        NotificacionesRef.child(CurrentUserId).push().setValue(chatNotificacion).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    enviarMensaje.setEnabled(true);
                                                    CurrentEstado="viejo";
                                                    enviarMensaje.setText("Cancelar Solicitud");
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
    }
}