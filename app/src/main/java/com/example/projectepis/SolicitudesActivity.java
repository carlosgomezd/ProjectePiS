package com.example.projectepis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SolicitudesActivity extends AppCompatActivity {

    private RecyclerView SolicitudesRecyclerView;
    private DatabaseReference SolicitudesRef, UserRef, ContactosRef;
    private FirebaseAuth auth;
    private String CurrentUserId;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes);

        SolicitudesRecyclerView =(RecyclerView) findViewById(R.id.ver_solicitudes_recyclerView);
        SolicitudesRecyclerView.setLayoutManager(new LinearLayoutManager(SolicitudesActivity.this));

        SolicitudesRef = FirebaseDatabase.getInstance().getReference().child("Solicitudes");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ContactosRef = FirebaseDatabase.getInstance().getReference().child("Contactos");
        auth = FirebaseAuth.getInstance();
        CurrentUserId=auth.getCurrentUser().getUid();

        toolbar = (Toolbar) findViewById(R.id.ver_solicitudes_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ver solicitudes de amistad");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contactos> options = new FirebaseRecyclerOptions.Builder<Contactos>()
                .setQuery(SolicitudesRef.child(CurrentUserId), Contactos.class).build();
        FirebaseRecyclerAdapter<Contactos, SolicitudesViewHolder> adapter = new FirebaseRecyclerAdapter<Contactos, SolicitudesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SolicitudesViewHolder holder, int position, @NonNull Contactos model) {
                holder.itemView.findViewById(R.id.user_solicitud_aceptar_boton).setVisibility(View.VISIBLE);
                holder.itemView.findViewById(R.id.user_solicitud_cancelar_boton).setVisibility(View.VISIBLE);

                final String user_id = getRef(position).getKey();
                DatabaseReference getTipo = getRef(position).child("tipo").getRef();
                getTipo.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){
                            String tipo = snapshot.getValue().toString();
                            if(tipo.equals("recibido")){
                                UserRef.child(user_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.hasChild("imagen")){
                                            String img = snapshot.child("imagen").getValue().toString();;
                                            Picasso.get().load(img).placeholder(R.drawable.user).into(holder.images);
                                        }
                                        String nom = snapshot.child("nombre").getValue().toString();
                                        String cogn = snapshot.child("apellido").getValue().toString();
                                        holder.nombres.setText(nom);
                                        holder.apellidos.setText(cogn);
                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                CharSequence opciones[] = new CharSequence[]{
                                                        "Aceptar",
                                                        "Cancelar"
                                                };
                                                AlertDialog.Builder builder = new AlertDialog.Builder(SolicitudesActivity.this);
                                                builder.setTitle("Solicitud de "+nom);
                                                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        if(i == 0){
                                                            ContactosRef.child(CurrentUserId).child(user_id).child("Contacto")
                                                                    .setValue("Aceptado").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        ContactosRef.child(user_id).child(CurrentUserId).child("Contacto")
                                                                                .setValue("Aceptado").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()){
                                                                                    SolicitudesRef.child(CurrentUserId).child(user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if(task.isSuccessful()){
                                                                                                SolicitudesRef.child(user_id).child(CurrentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        Toast.makeText(SolicitudesActivity.this, "Nuevo Contacto Agregado", Toast.LENGTH_SHORT).show();
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
                                                        }if(i == 1){
                                                            SolicitudesRef.child(CurrentUserId).child(user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        SolicitudesRef.child(user_id).child(CurrentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                Toast.makeText(SolicitudesActivity.this, "Solicitud Eliminada", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                                builder.show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {}});
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}});

            }

            @NonNull
            @Override
            public SolicitudesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout, parent, false);
                SolicitudesViewHolder viewHolder = new SolicitudesViewHolder(view);
                return viewHolder;
            }
        };
        SolicitudesRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private static class SolicitudesViewHolder extends RecyclerView.ViewHolder{
        TextView nombres, apellidos;
        ImageView images;
        Button aceptar, cancelar;
        public SolicitudesViewHolder(@NonNull View itemView) {
            super(itemView);
            nombres = itemView.findViewById(R.id.user_name);
            apellidos = itemView.findViewById(R.id.user_apellido);
            images = itemView.findViewById(R.id.user_imagen_perfil);
            aceptar = itemView.findViewById(R.id.user_solicitud_aceptar_boton);
            cancelar = itemView.findViewById(R.id.user_solicitud_cancelar_boton);
        }
    }
}