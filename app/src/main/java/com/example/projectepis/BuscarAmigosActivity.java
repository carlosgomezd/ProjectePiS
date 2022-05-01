package com.example.projectepis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class BuscarAmigosActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView BuscarAmigosRecyclerView;
    private DatabaseReference UserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_amigos);

        BuscarAmigosRecyclerView = (RecyclerView) findViewById(R.id.buscar_amigos_recyclerView);
        BuscarAmigosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        toolbar = (Toolbar) findViewById(R.id.buscar_amigos_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Buscar Amigos");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contactos> options = new FirebaseRecyclerOptions.Builder<Contactos>().setQuery(UserRef, Contactos.class).build();
        FirebaseRecyclerAdapter<Contactos, BuscarAmigosViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contactos, BuscarAmigosViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull BuscarAmigosViewHolder holder, int position, @NonNull Contactos model) {

                        holder.nombreu.setText(model.getNombre());
                        holder.apellidou.setText(model.getApellido());
                        Picasso.get().load(model.getImagen()).placeholder(R.drawable.user).into(holder.imageu);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String usuario_id = getRef(position).getKey();

                                Intent intent = new Intent(BuscarAmigosActivity.this, PerfilUsuarioActivity.class);
                                intent.putExtra("usuario_id", usuario_id);
                                startActivity(intent);

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public BuscarAmigosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout, parent, false);
                        BuscarAmigosViewHolder viewHolder = new BuscarAmigosViewHolder(view);
                        return viewHolder;
                    }
                };
        BuscarAmigosRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class BuscarAmigosViewHolder extends RecyclerView.ViewHolder{

        TextView nombreu, apellidou;
        ImageView imageu;
        public BuscarAmigosViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreu = itemView.findViewById(R.id.user_name);
            apellidou = itemView.findViewById(R.id.user_apellido);
            imageu = itemView.findViewById(R.id.user_imagen_perfil);
        }
    }
}