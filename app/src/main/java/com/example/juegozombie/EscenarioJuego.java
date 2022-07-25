package com.example.juegozombie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.juegozombie.commons.Constantes;
import com.example.juegozombie.commons.Disegno;
import com.example.juegozombie.dialog.DialogFragment;
import com.example.juegozombie.entities.Jugador;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
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
    private Button btnPlay;
    private ImageView imagen;

    private MovimientoZombie movimientoZombie;
    private FirebaseAuth auth;
    private FirebaseDatabase dataBase;
    private DatabaseReference dbReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escenario_juego);
        initApp();
    }

    private  void initApp(){
        findByWidget();
        initFirebase();
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
        btnPlay= findViewById(R.id.btnPlay);
        lottieAnimacion= findViewById(R.id.lottieMovimiento);
        lottieAnimacion.setRepeatCount(LottieDrawable.INFINITE);

        imagen = findViewById(R.id.imageGif);
    }
    private void  initFirebase(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        dataBase = FirebaseDatabase.getInstance();
        dbReference = dataBase.getReference(Constantes.NAME_BD);
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
        updateDataPlayer();
        dialogSms();
        // tiene que ir despues de DialogSms()
        contador =0;
        txtContador.setText("0");
    }
    private void dialogSms(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment  dialog = new DialogFragment(String.valueOf(contador));
        dialog.setCancelable(false);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.add(android.R.id.content, dialog)
                .addToBackStack(null).commit();


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

    private  void updateDataPlayer(){

        Map<String, Object> data =  new HashMap<>();
        data.put("Zombies", contador);
        dbReference.child(user.getUid()).updateChildren(data).addOnCompleteListener( (task) ->{
            Toast.makeText(this, "El puntaje ha sido actualizado correctamente", Toast.LENGTH_SHORT).show();
        });

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