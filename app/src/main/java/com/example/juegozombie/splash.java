package com.example.juegozombie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.juegozombie.commons.Constantes;

public class splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        lauchSplash();
    }
    private void lauchSplash(){

        /**
         * Handler() permite ejecutar un codigo en cierta cantidad de segundos
         */

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent  = new Intent(splash.this, MainActivity.class);
                startActivity(intent);
            }
        }, Constantes.DURACION_SPLASH);

    }
}