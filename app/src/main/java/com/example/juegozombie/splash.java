package com.example.juegozombie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.juegozombie.commons.Constantes;
import com.example.juegozombie.commons.Disegno;

public class splash extends AppCompatActivity {

    private TextView txtTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        iniApp();
    }
    private void iniApp(){
        findByWidget();
        lauchSplash();
        setTypeFont();
    }
    private void findByWidget(){
        txtTitle = findViewById(R.id.title);
    }

    private void setTypeFont(){
        Typeface typeface = Disegno.getTypeFace(this);
        txtTitle.setTypeface(typeface);
    }
    private void lauchSplash() {

        /**
         * Handler() permite ejecutar un codigo en cierta cantidad de segundos
         */

        new Handler().postDelayed(() -> {

            Intent intent = new Intent(splash.this, Menu.class);
            startActivity(intent);
        }, Constantes.DURACION_SPLASH);

    }
}