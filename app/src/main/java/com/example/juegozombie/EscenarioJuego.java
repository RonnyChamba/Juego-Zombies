package com.example.juegozombie;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.juegozombie.commons.Disegno;
import com.example.juegozombie.entities.Jugador;

public class EscenarioJuego extends AppCompatActivity {

    private Jugador currentPlayer;
    private TextView txtContador;
    private TextView txtUid;
    private TextView txtNombre;
    private TextView txtTiempo;

    private ImageView imgZombie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escenario_juego);
        initApp();
    }

    private  void initApp(){
        findByWidget();
        setTypeFont();
        getDataPlayer();
    }
    private void findByWidget() {

        txtContador = findViewById(R.id.txtContadorEsc);
        txtNombre = findViewById(R.id.txtNombreEs);
        txtTiempo= findViewById(R.id.txtTiempoEsc);
        imgZombie = findViewById(R.id.imgJuego);
    }

    private void setTypeFont(){
        Typeface typeface = Disegno.getTypeFace(this);
        txtNombre.setTypeface(typeface);
        txtContador.setTypeface(typeface);
        txtTiempo.setTypeface(typeface);
    }

    private void  getDataPlayer(){


        Bundle bundle = getIntent().getExtras();
         currentPlayer = (Jugador) bundle.get("jugadorActual");
        txtNombre.setText(currentPlayer.getNombres());
       // txtUid.setText(currentPlayer.getuId());
        txtContador.setText(""+ currentPlayer.getPuntaje());
    }

}