package com.example.juegozombie;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.juegozombie.commons.Disegno;
import com.example.juegozombie.entities.Jugador;

public class EscenarioJuego extends AppCompatActivity implements View.OnClickListener {

    private Jugador currentPlayer;
    private TextView txtContador;
    private TextView txtUid;
    private TextView txtNombre;
    private TextView txtTiempo;

    private ImageView imgZombie;

    private int widthDisplay; // anchoPantalla
    private int heightDisplay; // altoPantalla
    private  int contador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escenario_juego);
        initApp();
    }

    private  void initApp(){
        findByWidget();
        setTypeFont();
        setListenerClick();
        getDataPlayer();
        sizeDisplay();
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
    private void setListenerClick(){
        imgZombie.setOnClickListener(this);
    }

    private void  getDataPlayer(){
        Bundle bundle = getIntent().getExtras();
         currentPlayer = (Jugador) bundle.get("jugadorActual");
        txtNombre.setText(currentPlayer.getNombres());
       // txtUid.setText(currentPlayer.getuId());
        txtContador.setText(""+ currentPlayer.getPuntaje());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == imgZombie.getId()) killerZombie();
    }

    private void killerZombie(){
        contador ++;
        txtContador.setText(String.valueOf(contador));

         imgZombie.setImageResource(R.drawable.zombie_muerto);

         new Handler().postDelayed((  ()-> {
             imgZombie.setImageResource(R.drawable.icono_app);
         }),500);

    }

    private void sizeDisplay(){ // pantalla

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        heightDisplay = point.y;
        widthDisplay = point.x;
        
    }
}