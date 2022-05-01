package com.example.projectepis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class VerAmigosActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView ContactosList;
    private DatabaseReference ContactosRef, UserRef;
    private FirebaseAuth auth;
    private String CurrentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_amigos);

        auth = FirebaseAuth.getInstance();
        CurrentUserId = auth.getCurrentUser().getUid();
        ContactosRef = FirebaseDatabase.getInstance().getReference().child("Contactos").child(CurrentUserId);
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ContactosList = (RecyclerView) findViewById(R.id.ver_amigos_recyclerView);
        ContactosList.setLayoutManager(new LinearLayoutManager(this));

        toolbar = (Toolbar) findViewById(R.id.ver_amigos_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ver Amigos");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Contactos>()
                .setQuery(ContactosRef, Contactos.class).build();
        FirebaseRecyclerAdapter<Contactos, ContactosViewHolder> adapter= new FirebaseRecyclerAdapter<Contactos, ContactosViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ContactosViewHolder holder, int position, @NonNull Contactos model) {
                String userIds = getRef(position).getKey();
                ContactosRef.child(userIds).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("imagen")){
                            String nombreUsuario = snapshot.child("nombre").getValue().toString();
                            String apellidoUsuario = snapshot.child("apellido").getValue().toString();
                            String imagenUsuario = snapshot.child("imagen").getValue().toString();
                            holder.nombre.setText(nombreUsuario);
                            holder.apellido.setText(apellidoUsuario);
                            Picasso.get().load(imagenUsuario).placeholder(R.drawable.user).into(holder.imagen);
                        }else{
                            String nombreUsuario = snapshot.child("nombre").getValue().toString();
                            String apellidoUsuario = snapshot.child("apellido").getValue().toString();
                            holder.nombre.setText(nombreUsuario);
                            holder.apellido.setText(apellidoUsuario);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}});
            }

            @NonNull
            @Override
            public ContactosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout, parent, false);
                ContactosViewHolder viewHolder = new ContactosViewHolder(view);
                return viewHolder;
            }
        };
        ContactosList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ContactosViewHolder extends RecyclerView.ViewHolder{
        TextView nombre, apellido;
        ImageView imagen;
        public ContactosViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.user_name);
            apellido = itemView.findViewById(R.id.user_apellido);
            imagen = itemView.findViewById(R.id.user_imagen_perfil);

        }
    }
}