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

        new Handler().postDelayed(() -> {
            
            Intent intent  = new Intent(splash.this, Menu.class);
            startActivity(intent);
        }, Constantes.DURACION_SPLASH);

    }
}