package com.example.projectepis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private String recibirUserId, nombre, apellido, imagen;
    private TextView nombreUsuario, apellidoUsuario;
    private ImageView imagenUsuario, botonEnviar;
    private Toolbar toolbar;
    private EditText mensaje;
    private DatabaseReference RootRef;
    private FirebaseAuth auth;
    private String EnviarUserId;

    private final List<Mensajes> mensajesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MensajeAdapter mensajeAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        auth=FirebaseAuth.getInstance();
        EnviarUserId=auth.getCurrentUser().getUid();
        RootRef= FirebaseDatabase.getInstance().getReference();
        recibirUserId = getIntent().getExtras().get("user_id").toString();
        nombre = getIntent().getExtras().get("user_name").toString();
        apellido = getIntent().getExtras().get("user_apellido").toString();
        imagen = getIntent().getExtras().get("user_imagen").toString();

        IniciarLayout();

        nombreUsuario.setText(nombre);
        apellidoUsuario.setText(apellido);
        Picasso.get().load(imagen).placeholder(R.drawable.user).into(imagenUsuario);

        mensajeAdapter = new MensajeAdapter(mensajesList);
        recyclerView =(RecyclerView) findViewById(R.id.listamensajesrecycler);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mensajeAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RootRef.child("Mensajes").child(EnviarUserId).child(recibirUserId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensajes mensajes = snapshot.getValue(Mensajes.class);
                mensajesList.add(mensajes);
                mensajeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void IniciarLayout() {
        toolbar=(Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_chat_bar, null);
        actionBar.setCustomView(view);

        nombreUsuario=(TextView) findViewById(R.id.usuario_nombre);
        apellidoUsuario=(TextView) findViewById(R.id.usuario_apellido);
        imagenUsuario=(ImageView) findViewById(R.id.usuario_imagen);
        mensaje =(EditText) findViewById(R.id.mensaje);
        botonEnviar=(ImageView) findViewById(R.id.enviar_mensaje_boton);
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnviarMensaje();
            }
        });

    }

    private void EnviarMensaje() {
        String mensajeText = mensaje.getText().toString();
        if(TextUtils.isEmpty(mensajeText)){
            Toast.makeText(this, "Escribe su mensaje", Toast.LENGTH_SHORT).show();
        }else{
            String mensajeEnviadoRef = "Mensajes/"+ EnviarUserId+"/"+recibirUserId;
            String mensajeRecibidoRef = "Mensajes/"+ recibirUserId+"/"+EnviarUserId;

            DatabaseReference usuarioMensajeRef = RootRef.child("Mensajes").child(EnviarUserId).child(recibirUserId).push();
            String mensajePushId = usuarioMensajeRef.getKey();

            Map mensajeTxt = new HashMap();
            mensajeTxt.put("mensaje", mensajeText);
            mensajeTxt.put("tipo", "texto");
            mensajeTxt.put("de", EnviarUserId);
            Map mensajeTxtFull = new HashMap();
            mensajeTxtFull.put(mensajeEnviadoRef+"/"+mensajePushId, mensajeTxt);
            mensajeTxtFull.put(mensajeRecibidoRef+"/"+mensajePushId, mensajeTxt);
            RootRef.updateChildren(mensajeTxtFull).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ChatActivity.this, "Mensaje Enviado", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ChatActivity.this, "Error al enviar", Toast.LENGTH_SHORT).show();
                    }mensaje.setText("");
                }
            });
        }
    }
}