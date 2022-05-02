package com.example.projectepis;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class ChatFragment extends Fragment {

    private View ChatViewUnica;
    private RecyclerView ChatLista;
    private DatabaseReference ContactosRef, UserRef;
    private FirebaseAuth auth;
    private String CurrentUserId;
    private String imagenS = "default";

    public ChatFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        CurrentUserId=auth.getCurrentUser().getUid();
        ContactosRef= FirebaseDatabase.getInstance().getReference().child("Contactos").child(CurrentUserId);
        UserRef= FirebaseDatabase.getInstance().getReference().child("Users");

        ChatViewUnica = inflater.inflate(R.layout.fragment_chat, container, false);
        ChatLista=(RecyclerView) ChatViewUnica.findViewById(R.id.chat_lista);
        ChatLista.setLayoutManager(new LinearLayoutManager(getContext()));
        return ChatViewUnica;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Contactos> options = new FirebaseRecyclerOptions.Builder<Contactos>()
                .setQuery(ContactosRef, Contactos.class).build();
        FirebaseRecyclerAdapter<Contactos, ChatsViewHolder> adapter = new FirebaseRecyclerAdapter<Contactos, ChatsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatsViewHolder holder, int position, @NonNull Contactos model) {
                final String userIds = getRef(position).getKey();
                UserRef.child(userIds).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            if (snapshot.hasChild("imagen")){
                                imagenS = snapshot.child("imagen").getValue().toString();
                                Picasso.get().load(imagenS).placeholder(R.drawable.user).into(holder.imagenC);
                            }final String nombreS = snapshot.child("nombre").getValue().toString();
                            final String apellidoS = snapshot.child("apellido").getValue().toString();
                            holder.nombreC.setText(nombreS);
                            holder.apellidoC.setText(apellidoS);

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getContext(), ChatActivity.class);
                                    intent.putExtra("user_id", userIds);
                                    intent.putExtra("user_name", nombreS);
                                    intent.putExtra("user_apellido", apellidoS);
                                    intent.putExtra("user_imagen", imagenS);
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}});
            }

            @NonNull
            @Override
            public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout, parent, false);
                return new ChatsViewHolder(view);
            }
        };
        ChatLista.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder{
        ImageView imagenC;
        TextView nombreC, apellidoC;
        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenC=itemView.findViewById(R.id.user_imagen_perfil);
            nombreC=itemView.findViewById(R.id.user_name);
            apellidoC=itemView.findViewById(R.id.user_apellido);
        }
    }
}