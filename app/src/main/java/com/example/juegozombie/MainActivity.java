package com.example.juegozombie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    }
    private void  findByWidget(){

        btnLogin = findViewById(R.id.btnLogin);
        btnRegistro = findViewById(R.id.btnRegistro);

    }
    private void setListenerClick(){

        btnLogin.setOnClickListener(this);
        btnRegistro.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        Intent intent = null;

        if (id == btnLogin.getId()){

        }else if (id == btnRegistro.getId()){
            intent = new Intent(this, Registro.class);
        }
        startActivity(intent);

    }
}