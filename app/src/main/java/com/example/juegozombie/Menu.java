package com.example.juegozombie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu extends AppCompatActivity {


    private FirebaseAuth auth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initApp();

    }

    @Override
    protected void onStart() {
        usuarioLogeado();
        super.onStart();
    }

    private void initApp(){

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }
    private void usuarioLogeado(){

        if (user !=null){
            Toast.makeText(this, "en linea", Toast.LENGTH_LONG).show();
        }else{
            startActivity( new Intent(this, MainActivity.class));
            finish();
        }

    }
}