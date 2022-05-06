package com.example.projectepis;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.MensajesViewHolder> {

    private List<Mensajes> usuarioMensajes;
    private FirebaseAuth auth;
    private String CurrentUserId;
    private DatabaseReference UserRef;

    public MensajeAdapter(List<Mensajes> usuarioMensajes){
        this.usuarioMensajes = usuarioMensajes;
    }

    public class MensajesViewHolder extends RecyclerView.ViewHolder{

        public TextView enviarMensajeTexto, recibirMensajeTexto;
        public ImageView mensajeImagenEnviar, mensajeImagenRecibir;

        public MensajesViewHolder(@NonNull View itemView) {
            super(itemView);

            enviarMensajeTexto =(TextView) itemView.findViewById(R.id.enviar_mensaje);
            recibirMensajeTexto =(TextView) itemView.findViewById(R.id.recibir_mensaje);
            mensajeImagenEnviar = (ImageView) itemView.findViewById(R.id.imagen_mensaje_enviar);
            mensajeImagenRecibir = (ImageView) itemView.findViewById(R.id.imagen_mensaje_recibir);

        }
    }

    @NonNull
    @Override
    public MensajesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usuario_mensaje_layout, parent, false);
        auth=FirebaseAuth.getInstance();
        CurrentUserId = auth.getCurrentUser().getUid();

        return new MensajesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajesViewHolder holder, int position) {
        Mensajes mensajes = usuarioMensajes.get(position);

        String DeUsuarioId = mensajes.getDe();
        String TipoMensaje = mensajes.getTipo();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(DeUsuarioId);
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}});
        holder.recibirMensajeTexto.setVisibility(View.GONE);
        holder.enviarMensajeTexto.setVisibility(View.GONE);
        holder.mensajeImagenRecibir.setVisibility(View.GONE);
        holder.mensajeImagenEnviar.setVisibility(View.GONE);
        if(TipoMensaje.equals("texto")){

            if(DeUsuarioId.equals(CurrentUserId)){
                holder.enviarMensajeTexto.setVisibility(View.VISIBLE);
                holder.enviarMensajeTexto.setBackgroundResource(R.drawable.enviar_mensaje);
                holder.enviarMensajeTexto.setTextColor(Color.WHITE);
                holder.enviarMensajeTexto.setText(mensajes.getMensaje() + "\n\n"+mensajes.getHora());
            }else{
                holder.recibirMensajeTexto.setVisibility(View.VISIBLE);
                holder.recibirMensajeTexto.setBackgroundResource(R.drawable.recibir_mensaje);
                holder.recibirMensajeTexto.setTextColor(Color.BLACK);
                holder.recibirMensajeTexto.setText(mensajes.getMensaje() + "\n\n"+mensajes.getHora());
            }
        }else if(TipoMensaje.equals("imagen")){
            if(DeUsuarioId.equals(CurrentUserId)){
                holder.mensajeImagenEnviar.setVisibility(View.VISIBLE);
                Picasso.get().load(mensajes.getMensaje()).into(holder.mensajeImagenEnviar);
            }else{
                holder.mensajeImagenRecibir.setVisibility(View.VISIBLE);
                Picasso.get().load(mensajes.getMensaje()).into(holder.mensajeImagenRecibir);
            }
        }else{
            if(DeUsuarioId.equals(CurrentUserId)){
                holder.mensajeImagenEnviar.setVisibility(View.VISIBLE);
                holder.mensajeImagenEnviar.setBackgroundResource(R.drawable.archivopdf);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(usuarioMensajes.get(position).getMensaje()));
                        holder.itemView.getContext().startActivity(intent);
                    }
                });
            }else{
                holder.mensajeImagenRecibir.setVisibility(View.VISIBLE);
                holder.mensajeImagenRecibir.setBackgroundResource(R.drawable.archivopdf);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(usuarioMensajes.get(position).getMensaje()));
                        holder.itemView.getContext().startActivity(intent);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return usuarioMensajes.size();
    }



}
