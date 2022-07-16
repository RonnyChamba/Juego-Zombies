package com.example.juegozombie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.juegozombie.commons.Disegno;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private Button btnRegistro;
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
    }
    private void  findByWidget(){

        btnLogin = findViewById(R.id.btnLogin);
        btnRegistro = findViewById(R.id.btnRegistro);

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