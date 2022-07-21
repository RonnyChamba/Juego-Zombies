package com.example.juegozombie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.juegozombie.commons.Disegno;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private Button btnRegistro;
    private ImageView imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initApp();
    }

    private void  initApp(){
        findByWidget();
        setListenerClick();
        setTypeFont();
        onloadGif();
    }
    private void  findByWidget(){

        btnLogin = findViewById(R.id.btnLogin);
        btnRegistro = findViewById(R.id.btnRegistro);
        imagen = findViewById(R.id.imageGif);
    }
    private void setListenerClick(){

        btnLogin.setOnClickListener(this);
        btnRegistro.setOnClickListener(this);
    }
    private void setTypeFont(){
        Typeface typeface = Disegno.getTypeFace(this);
        btnLogin.setTypeface(typeface);
        btnRegistro.setTypeface(typeface);
    }
    private void onloadGif(){

        //String url = "https://c.tenor.com/FkC4OX_XzowAAAAC/calabaza-pumpkin.gif";
        String url = "https://i.pinimg.com/originals/35/37/56/3537568867d0b27733c47299f1f2e999.gif";
        Uri urlParse = Uri.parse(url);
        Glide.with(getApplicationContext()).load(urlParse).into(imagen);

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        Intent intent = null;

        if (id == btnLogin.getId()){
            intent = new Intent(this, Login.class);

        }else if (id == btnRegistro.getId()){
            intent = new Intent(this, Registro.class);
        }
        startActivity(intent);

    }
}