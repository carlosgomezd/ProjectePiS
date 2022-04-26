package com.example.projectepis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.EmailAuthCredential;

public class Chat extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager myviewPager;
    private TabLayout mytabLayout;
    private AccesoTabsAdapter myaccesoTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar=(Toolbar)findViewById(R.id.app_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AnyDay");

        myviewPager = (ViewPager)findViewById(R.id.main_tabs_pager);
        myaccesoTabsAdapter = new AccesoTabsAdapter(getSupportFragmentManager());
        myviewPager.setAdapter(myaccesoTabsAdapter);

        mytabLayout = (TabLayout)findViewById(R.id.main_tabs);
        mytabLayout.setupWithViewPager(myviewPager);
    }

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
        }
        if(item.getItemId() == R.id.ayuda_menu){
            Toast.makeText(this, "Ayuda", Toast.LENGTH_SHORT).show();
        }
        if(item.getItemId() == R.id.cerrar_sesion_menu){

        }
        return true;
    }
}