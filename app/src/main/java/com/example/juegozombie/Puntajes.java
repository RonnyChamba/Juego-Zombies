package com.example.juegozombie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.juegozombie.commons.Constantes;
import com.example.juegozombie.entities.Jugador;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Puntajes extends AppCompatActivity {


    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerViewUsuarios;
    private  Adaptador adaptador;
    private List<Jugador> usuarioList;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntajes);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Puntajes");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        layoutManager = new LinearLayoutManager(this);
        firebaseAuth = FirebaseAuth.getInstance();

        recyclerViewUsuarios = findViewById(R.id.recyclerViewUsuarios);


        layoutManager.setReverseLayout(true); // ordena Z-A
        layoutManager.setStackFromEnd(true);  // Empieza desde arriab sin tener que subir
        recyclerViewUsuarios.setHasFixedSize(true);

        recyclerViewUsuarios.setLayoutManager(layoutManager);

        usuarioList = new ArrayList<>();

        ontenerTodosUsuarios();


    }

    private void ontenerTodosUsuarios() {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constantes.NAME_BD);

        reference.orderByChild("Zombies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usuarioList.clear();

                for (DataSnapshot ds : snapshot.getChildren()){

                    //Jugador jugador = ds.getValue(Jugador.class);

                    Jugador jugador  = new Jugador();

                    jugador.setNombres(ds.child("Nombres").getValue().toString());
                    jugador.setEmail(ds.child("Email").getValue().toString());
                    jugador.setPuntaje( Integer.parseInt( ds.child("Zombies").getValue().toString()));
                    jugador.setPais(ds.child("Pais").getValue().toString());
                    jugador.setImagen(ds.child("Imagen").getValue().toString());

                    usuarioList.add(jugador);
                    Collections.sort(usuarioList);
                    adaptador = new Adaptador(Puntajes.this, usuarioList);
                    recyclerViewUsuarios.setAdapter(adaptador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return super.onSupportNavigateUp();
    }
}