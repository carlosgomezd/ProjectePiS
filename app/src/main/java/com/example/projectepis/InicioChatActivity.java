package com.example.projectepis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InicioChatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager myviewPager;
    private TabLayout mytabLayout;
    private AccesoTabsAdapter myaccesoTabsAdapter;
    private DatabaseReference UserRef, RootRef;
    private FirebaseAuth mAuth;
    private String CurrentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_chat);

        toolbar=(Toolbar)findViewById(R.id.app_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AnyDay");

        myviewPager = (ViewPager)findViewById(R.id.main_tabs_pager);
        myaccesoTabsAdapter = new AccesoTabsAdapter(getSupportFragmentManager());
        myviewPager.setAdapter(myaccesoTabsAdapter);

        mytabLayout = (TabLayout)findViewById(R.id.main_tabs);
        mytabLayout.setupWithViewPager(myviewPager);

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        RootRef = FirebaseDatabase.getInstance().getReference().child("Grupos");
        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.chat);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.calendario:
                    Intent intent = new Intent(InicioChatActivity.this, Calendario.class);
                    startActivity(intent);
                    break;
                case R.id.crear_evento:
                    Intent intent2 = new Intent(InicioChatActivity.this, CrearEvento.class);
                    startActivity(intent2);
                    break;
                case R.id.evento_destacado:
                    Intent intent3 = new Intent(InicioChatActivity.this, EventoDestacado.class);
                    startActivity(intent3);
                    break;
                case R.id.perfil:
                    //Intent intent = new Intent(InicioChatActivity.this, CalendarioActivity.class);
                    //startActivity(intent);
                    break;
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menus_opciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.buscar_contactos_menu){
            Toast.makeText(this, "Buscar Amigos", Toast.LENGTH_SHORT).show();
            BuscarAmigos();
        }
        if(item.getItemId() == R.id.ver_contactos_menu){
            Toast.makeText(this, "Ver Amigos", Toast.LENGTH_SHORT).show();
            VerAmigos();
        }
        if(item.getItemId() == R.id.ver_solicitudes_menu){
            Toast.makeText(this, "Ver Solicitudes de Amistad", Toast.LENGTH_SHORT).show();
            VerSolicitudes();
        }
        if(item.getItemId() == R.id.crear_grupo_menu){
            CrearNuevoGrupo();
        }
        if(item.getItemId() == R.id.ayuda_menu){
            Toast.makeText(this, "Ayuda", Toast.LENGTH_SHORT).show();
        }
        if(item.getItemId() == R.id.cerrar_sesion_menu){
            //Toast.makeText(this, "Cerrar Sesion", Toast.LENGTH_SHORT).show();

            //finish();
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(InicioChatActivity.this, "Sesion cerrada", Toast.LENGTH_SHORT ).show();
            Intent intent = new Intent(InicioChatActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }

    private void VerSolicitudes() {
        Intent intent = new Intent(InicioChatActivity.this, SolicitudesActivity.class);
        startActivity(intent);
    }

    private void BuscarAmigos() {
        Intent intent = new Intent(InicioChatActivity.this, BuscarAmigosActivity.class);
        startActivity(intent);
    }
    private void VerAmigos() {
        Intent intent = new Intent(InicioChatActivity.this, VerAmigosActivity.class);
        startActivity(intent);
    }

    private void CrearNuevoGrupo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InicioChatActivity.this, R.style.AlertDialog);
        builder.setTitle("Nombre del grupo: ");

        final EditText nombregrupo = new EditText(InicioChatActivity.this);
        builder.setView(nombregrupo);

        builder.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nombregrup = nombregrupo.getText().toString();

                if(TextUtils.isEmpty(nombregrup)){
                    Toast.makeText(InicioChatActivity.this, "Introduce el nombre del grupo", Toast.LENGTH_SHORT).show();
                }else{
                    CrearGrupoFirebase(nombregrup);
                }
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void CrearGrupoFirebase(String nombregrup) {

        RootRef.child(nombregrup).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(InicioChatActivity.this, "Grupo creado", Toast.LENGTH_SHORT).show();
                }else{
                    String error = task.getException().getMessage().toString();
                    Toast.makeText(InicioChatActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}