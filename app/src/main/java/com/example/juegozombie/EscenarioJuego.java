package com.example.juegozombie;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
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

    private Button btnPlay;
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
    }

    private void setTypeFont(){
        Typeface typeface = Disegno.getTypeFace(this);
        txtNombre.setTypeface(typeface);
        txtContador.setTypeface(typeface);
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

        if (id == imgZombie.getId()) killerZombie();
        else if (id == btnPlay.getId()) iniciarJuego();
    }
    private void killerZombie(){
        contador ++;
        txtContador.setText(String.valueOf(contador));

         imgZombie.setImageResource(R.drawable.zombie_muerto);

        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        v.vibrate(300);

        /* new Handler().postDelayed((  ()-> {

             //imgZombie.setImageResource(R.drawable.icono_app);
            // moveZombie();
         }),delayZombie);*/

    }
    private void iniciarJuego(){
        starPlay();
        cuentaAtras();
        starAnimationLottie();
    }

    private void starAnimationLottie(){
        lottieAnimacion.setRepeatCount(LottieDrawable.INFINITE);
        lottieAnimacion.playAnimation();
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

        MovimientoZombie movi = new MovimientoZombie();
        movi.start();
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
                    moveZombie();
                }

            }, 1000, 1000);

        }
    }

}