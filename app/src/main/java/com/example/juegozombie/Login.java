package com.example.juegozombie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity  implements View.OnClickListener {


    private EditText txtEmailLogin;
    private EditText txtPassLogin;

    private Button btnLogin;

    private FirebaseAuth auth; // Firebase Autenticacion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initApp();
    }

    private void initApp(){
        findByWidget();
        setListenerClick();
        // auth.initializeApp(this);
        auth = FirebaseAuth.getInstance();
    }

    private void findByWidget(){
        txtEmailLogin = findViewById(R.id.emailLogin);
        txtPassLogin = findViewById(R.id.passLogin);
        btnLogin = findViewById(R.id.btnIngresarLogin);
    }

    private void setListenerClick(){

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == btnLogin.getId()) {
            login();
        }

    }
    private  void  login(){

        String email = txtEmailLogin.getText().toString();
        String password = txtPassLogin.getText().toString();

        if (validFields(email, password)){
            accederJugador(email, password);
        }
    }

    private boolean validFields(String email, String password){
        boolean retorno  = true;

      if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmailLogin.setError("Email invalido");
            txtEmailLogin.setFocusable(true);
            retorno  = false;

        } else if (password.trim().length()<6) {
            txtPassLogin.setError("Contraseña debe ser mayor a 6");
            txtPassLogin.setFocusable(true);
            retorno  = false;
        }

        return retorno;
    }

    private void accederJugador(String email, String password){

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener( (task) ->{

            if (task.isSuccessful()){
                FirebaseUser user = auth.getCurrentUser();
                startActivity(new Intent(Login.this, Menu.class));
                assert  user !=null;
                Toast.makeText(Login.this, "BIENVENIDO(A) "+ user.getEmail(), Toast.LENGTH_LONG).show();
                finish();
            }

        }).addOnFailureListener( (exception)->{
            //Toast.makeText(Login.this, exception.getMessage(), Toast.LENGTH_LONG).show();
            Toast.makeText(Login.this, "Jugador no registrado", Toast.LENGTH_LONG).show();
        });
    }

}
