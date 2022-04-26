package com.example.projectepis;
import android.content.Intent;
import android.icu.text.Edits;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GruposFragment extends Fragment {

    private View grupoFragmentoView;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> lista_grupos = new ArrayList<>();

    private DatabaseReference GrupoRef;

    public GruposFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        grupoFragmentoView = inflater.inflate(R.layout.fragment_grupos, container, false);
        GrupoRef = FirebaseDatabase.getInstance().getReference().child("Grupos");
        IniciarLista();

        MostrarListaGrupos();

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String CurrentGrupoNombre = adapterView.getItemAtPosition(i).toString();

                Intent intent = new Intent(getContext(), GrupoChatActivity.class);
                intent.putExtra("nombregrupo", CurrentGrupoNombre);
                startActivity(intent);
            }
        });

        return grupoFragmentoView;
    }

    private void MostrarListaGrupos() {
        GrupoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> set = new HashSet<>();
                Iterator iterator = snapshot.getChildren().iterator();

                while (iterator.hasNext()) {
                    set.add(((DataSnapshot) iterator.next()).getKey());
                }
                lista_grupos.clear();
                lista_grupos.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void IniciarLista() {
        list_view = (ListView) grupoFragmentoView.findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, lista_grupos);
        list_view.setAdapter(arrayAdapter);
    }
}


