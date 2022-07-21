package com.example.juegozombie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.bumptech.glide.Glide;
import com.example.juegozombie.commons.Disegno;
import com.example.juegozombie.entities.Jugador;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class EscenarioJuego extends AppCompatActivity implements View.OnClickListener {

    private Jugador currentPlayer;
    private TextView txtContador;
    private TextView txtUid;
    private TextView txtNombre;
    private TextView txtTiempo;
    private LottieAnimationView lottieAnimacion;
    private ImageView imgZombie;

    private Random random; // aleatorio
    private int widthDisplay; // anchoPantalla
    private int heightDisplay; // altoPantalla
    private  int contador;
    private  final int  delayZombie = 400;

    private boolean gameOver;
    private Dialog mensajeFinPartida; // miDialog
    private Button btnPlay;
    private ImageView imagen;

    private MovimientoZombie movimientoZombie;
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
        mensajeFinPartida = new Dialog(EscenarioJuego.this);
        //onloadGif();

        //starPlay();
        //cuentaAtras();
    }
    private void findByWidget() {

        txtContador = findViewById(R.id.txtContadorEsc);
        txtNombre = findViewById(R.id.txtNombreEs);
        txtTiempo= findViewById(R.id.txtTiempoEsc);
        imgZombie = findViewById(R.id.imgJuego);
        btnPlay= findViewById(R.id.btnPlay);
        lottieAnimacion= findViewById(R.id.lottieMovimiento);

        mensajeFinPartida = new Dialog(EscenarioJuego.this);
        lottieAnimacion.setRepeatCount(LottieDrawable.INFINITE);

        imagen = findViewById(R.id.imageGif);
    }

    private void  setTypeFont(){
        Typeface typeface = Disegno.getTypeFace(this);
        txtNombre.setTypeface(typeface);
        txtContador.setTypeface(typeface);
        btnPlay.setTypeface(typeface);
        txtTiempo.setTypeface(typeface);
    }
    private void setListenerClick(){
        imgZombie.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
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

        if (id == imgZombie.getId() && !gameOver)killerZombie();
        else if (id == btnPlay.getId()) iniciarJuego();
    }
    private void killerZombie(){
        contador ++;
        txtContador.setText(String.valueOf(contador));

         imgZombie.setImageResource(R.drawable.zombie_muerto);

        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        v.vibrate(300);
    }
    private void iniciarJuego(){
        starPlay();
        cuentaAtras();
    }
    // mensajeGameOver
    private void finJuego(){
        gameOver = true;
        lottieAnimacion.cancelAnimation();
        movimientoZombie.interrupt();
        mensajeGameOver();
    }

    private void mensajeGameOver() {

        Typeface typeface = Disegno.getTypeFace(this);

        TextView seAcaboTxt, hasMatadoTxt, numeroTxt;
        Button jugarDeNuevo, irMenu, puntajes;

        mensajeFinPartida.setContentView(R.layout.gameover);
        mensajeFinPartida.setCancelable(false);

        seAcaboTxt = mensajeFinPartida.findViewById(R.id.seacaboTxt);
        hasMatadoTxt = mensajeFinPartida.findViewById(R.id.hasMatadoTxt);
        numeroTxt = mensajeFinPartida.findViewById(R.id.numeroTxt);

        jugarDeNuevo = mensajeFinPartida.findViewById(R.id.jugarDeNuevo);
        irMenu = mensajeFinPartida.findViewById(R.id.irMenu);
        puntajes = mensajeFinPartida.findViewById(R.id.puntajes);

        String zombies = String.valueOf(contador);
        numeroTxt.setText(zombies);


        seAcaboTxt.setTypeface(typeface);
        hasMatadoTxt.setTypeface(typeface);
        numeroTxt.setTypeface(typeface);

        jugarDeNuevo.setTypeface(typeface);
        irMenu.setTypeface(typeface);
        puntajes.setTypeface(typeface);

        mensajeFinPartida.show();

    }
    private void onloadGif(){

        //String url = "https://c.tenor.com/FkC4OX_XzowAAAAC/calabaza-pumpkin.gif";
        String url = "https://i.pinimg.com/originals/35/37/56/3537568867d0b27733c47299f1f2e999.gif";
        Uri urlParse = Uri.parse(url);
        Glide.with(getApplicationContext()).load(urlParse).into(imagen);
    }
    private void sizeDisplay(){ // pantalla

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        heightDisplay = point.y;
        widthDisplay = point.x;
        random = new Random();
    }
    private void  cuentaAtras(){

        new CountDownTimer(10000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                long segundosRestantes =   millisUntilFinished/1000;
                txtTiempo.setText(segundosRestantes +" s");
            }

            @Override
            public void onFinish() {
                txtTiempo.setText("0s");
                finJuego();

            }
        }.start();
    }
    private void moveZombie(){// movimiento

        int min = 0;

        int maxX = widthDisplay - imgZombie.getWidth();
        int maxY = widthDisplay - imgZombie.getHeight();

        int randomX = random.nextInt(    ((maxX - min) + 1) + min );
        int randomY = random.nextInt(    ((maxY - min) + 1) + min );

        imgZombie.setX(randomX);
        imgZombie.setY(randomY);
    }
    private void starPlay(){

        lottieAnimacion.playAnimation();

        movimientoZombie = new MovimientoZombie();

        movimientoZombie.start();

        gameOver = false;

    }

    class MovimientoZombie extends Thread{

        public MovimientoZombie(){
        }

        @Override
        public void run() {

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    imgZombie.setImageResource(R.drawable.icono_app);
                    if (!gameOver) moveZombie();
                    else timer.cancel();
                }

            }, 1000, 1000);

        }
    }

}