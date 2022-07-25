package com.example.juegozombie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.juegozombie.commons.Constantes;
import com.example.juegozombie.commons.Disegno;
import com.example.juegozombie.entities.Jugador;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class Menu extends AppCompatActivity  implements View.OnClickListener {


    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference; // jugadores
    private Button btnJugar;
    private Button btnPuntuacion;
    private Button btnAcercaDe;
    private Button btnCerrarSesion;
    private Button btnEditar;
    private Button btnCambiasPass;


    private TextView txtTitleMenu;
    private TextView txtPerfilMenu;
    private TextView txtFechaMenu;
    private TextView txtEdadMenu;
    private TextView txtPaisMenu;
    private TextView txtZombieMenu;
    private TextView txtUidMenu;
    private TextView txtSubTitleMenu;
    private TextView txtCorreoJugaMenu;
    private TextView txtNombreJugaMenu;
    private Jugador currentPlayer;
    private ImageView imagen;
    private CircleImageView imgPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initApp();

    }

    @Override
    protected void onStart() {
        usuarioLogeado();
        super.onStart();
    }

    private void initApp() {
        findByWidget();
        setListenerClick();
        setTypeFont();
        initFirebase();
        onloadGif();
    }

    private void initFirebase(){

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(Constantes.NAME_BD);
    }

    private void findByWidget() {

        txtTitleMenu = findViewById(R.id.txtTitleMenu);
        txtPerfilMenu = findViewById(R.id.txtTitlePerfil);
        txtEdadMenu = findViewById(R.id.txtEdadMenu);
        txtPaisMenu = findViewById(R.id.txtPaisMenu);
        txtFechaMenu = findViewById(R.id.txtFechaMenu);
        txtUidMenu = findViewById(R.id.txtUidMenu);
        txtZombieMenu = findViewById(R.id.txtZombiesMenu);
        txtSubTitleMenu = findViewById(R.id.txtSubTitleMenu);
        txtCorreoJugaMenu = findViewById(R.id.txtCorreoJugadorMenu);
        txtNombreJugaMenu = findViewById(R.id.txtNombreJugadorMenu);
        imgPerfil = findViewById(R.id.imgPerfil);

        btnJugar = findViewById(R.id.btnJugar);
        btnPuntuacion = findViewById(R.id.btnPuntaciones);
        btnAcercaDe = findViewById(R.id.btnHacerca);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnEditar = findViewById(R.id.btnEditar);
        btnCambiasPass = findViewById(R.id.btnCambiarPass);
        imagen = findViewById(R.id.imageGif);
    }
    private void setListenerClick() {
        btnPuntuacion.setOnClickListener(this);
        btnAcercaDe.setOnClickListener(this);
        btnJugar.setOnClickListener(this);
        btnCerrarSesion.setOnClickListener(this);
        btnEditar.setOnClickListener(this);
        btnCambiasPass.setOnClickListener(this);
    }
    private void setTypeFont(){

        Typeface typeface = Disegno.getTypeFace(this);

        txtTitleMenu.setTypeface(typeface);
        txtPerfilMenu.setTypeface(typeface);
        txtEdadMenu.setTypeface(typeface);
        txtFechaMenu.setTypeface(typeface);
        txtPaisMenu.setTypeface(typeface);
        txtSubTitleMenu.setTypeface(typeface);
        txtCorreoJugaMenu.setTypeface(typeface);
        txtNombreJugaMenu.setTypeface(typeface);
        txtZombieMenu.setTypeface(typeface);
        btnJugar.setTypeface(typeface);
        btnPuntuacion.setTypeface(typeface);
        btnAcercaDe.setTypeface(typeface);
        btnCerrarSesion.setTypeface(typeface);
        btnEditar.setTypeface(typeface);
        btnCambiasPass.setTypeface(typeface);




    }

    private void onloadGif(){

        //String url = "https://c.tenor.com/FkC4OX_XzowAAAAC/calabaza-pumpkin.gif";
        String url = "https://i.pinimg.com/originals/35/37/56/3537568867d0b27733c47299f1f2e999.gif";
        Uri urlParse = Uri.parse(url);
        Glide.with(getApplicationContext()).load(urlParse).into(imagen);

    }
    private void usuarioLogeado() {

      //  auth.signOut();

        if (user != null) {
            consulta();
            Toast.makeText(this, "en linea", Toast.LENGTH_LONG).show();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
         if (id == btnJugar.getId()){
             jugar();

        }else if (id == btnPuntuacion.getId()){

         }else if (id == btnAcercaDe.getId()){
         }
         else if (id == btnCerrarSesion.getId()){
             cerrarSesion();
         }
    }
    private void cerrarSesion(){
        auth.signOut();
        startActivity(new Intent(Menu.this, MainActivity.class));
        Toast.makeText(this, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show();

    }

    private void consulta (){
        Query query = databaseReference.orderByChild("Email").equalTo(user.getEmail());

        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot ds = snapshot.getChildren().iterator().next();

                currentPlayer = new Jugador();

                currentPlayer.setuId(ds.child("Uid").getValue().toString());
                currentPlayer.setEdad( Integer.parseInt(ds.child("Edad").getValue().toString()));
                currentPlayer.setPais( ds.child("Pais").getValue().toString());
                currentPlayer.setFecha( ds.child("Fecha").getValue().toString());
                currentPlayer.setPuntaje( Integer.parseInt(ds.child("Zombies").getValue().toString()));
                currentPlayer.setNombres(ds.child("Nombres").getValue().toString());
                currentPlayer.setEmail(ds.child("Email").getValue().toString());
                currentPlayer.setImagen(ds.child("Imagen").getValue().toString());

                txtUidMenu.setText(currentPlayer.getuId());
                // Obliatorio convertir en String
                txtZombieMenu.setText(""+currentPlayer.getPuntaje());
                txtCorreoJugaMenu.setText(currentPlayer.getEmail());
                txtNombreJugaMenu.setText(currentPlayer.getNombres());
                txtEdadMenu.setText(currentPlayer.getEdad() +"");
                txtPaisMenu.setText(currentPlayer.getPais());
                txtFechaMenu.setText(currentPlayer.getFecha());

                if (!currentPlayer.getImagen().equals("")){
                    Picasso.get().load(currentPlayer.getImagen()).into(imgPerfil);
                }else Picasso.get().load(R.drawable.calabaza_login).into(imgPerfil);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void  jugar(){

        Intent intent = new Intent(this, EscenarioJuego.class);
        intent.putExtra("jugadorActual",  currentPlayer);
        startActivity(intent);
    }
}