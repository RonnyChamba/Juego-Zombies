package com.example.juegozombie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juegozombie.commons.Constantes;
import com.example.juegozombie.commons.Disegno;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtNombre;
    private TextView txtFecha;
    private TextView txtEdad;
    private Spinner txtPais;

    private Button btnRegistrar;

    private TextView txtTitle;
    private FirebaseAuth auth; // Firebase Autenticacion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        initApp();
    }

    private void initApp() {
        findByWidget();
        setListenerClick();
        setFecha();
        setTypeFont();

       // auth.initializeApp(this);
        auth = FirebaseAuth.getInstance();
    }

    private void findByWidget() {

        txtTitle = findViewById(R.id.txtTitleRegistro);
        txtEmail = findViewById(R.id.txtCorreo);
        txtNombre = findViewById(R.id.txtNombre);
        txtPassword = findViewById(R.id.txtPassword);
        txtFecha = findViewById(R.id.txtFecha);
        txtEdad = findViewById(R.id.txtEdad);
        txtPais = findViewById(R.id.txtPais);

        btnRegistrar = findViewById(R.id.btnRegistrar);
    }

    private void setFecha() {
        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat("d 'de' MMMM 'del'  yyyy");
        String stringFecha = fecha.format(date);
        txtFecha.setText(stringFecha);
    }

    private void setListenerClick() {
        btnRegistrar.setOnClickListener(this);
    }


    private void setTypeFont(){
        Typeface typeface = Disegno.getTypeFace(this);
        txtTitle.setTypeface(typeface);
        txtEmail.setTypeface(typeface);
        txtPassword.setTypeface(typeface);
        txtNombre.setTypeface(typeface);
        btnRegistrar.setTypeface(typeface);

    }
    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == btnRegistrar.getId()) savePlayer();
    }

    private void savePlayer() {

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (validFields(email, password)) {
            registerPlayFirebase(email, password);
        }

    }
    private boolean validFields(String email, String password){

        boolean retorno  = true;
        if (email.trim().isEmpty() ) {
            txtEmail.setError("Ingrese email", null);
            txtEmail.setFocusable(true);
            retorno  = false;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError("Email invalido");
            txtEmail.setFocusable(true);

            retorno  = false;
        } else if (password.trim().isEmpty()) {
            txtPassword.setError("Ingrese una contraseña", null);
            txtPassword.setFocusable(true);
            retorno  = false;

        } else if (password.trim().length() < 6) {
            txtPassword.setError("Constraseña debe ser mayor a 6");
            txtPassword.setFocusable(true);
            retorno  = false;
        }

        return retorno;
    }

    private void registerPlayFirebase(String email, String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((task) ->{
            // Si el jugador fue registrado correctamente
            if (task.isSuccessful()){
                armarJugador();
            }else
                Toast.makeText(Registro.this, "Ha ocurrido un error", Toast.LENGTH_LONG).show();

        })   // SI falla el registro
                .addOnFailureListener( (e) ->{
                    Toast.makeText(Registro.this, e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private void armarJugador() {

        FirebaseUser user = auth.getCurrentUser();
        int contador = 0;
        assert  user !=null;

        String uidString = user.getUid();
        String nombre = txtNombre.getText().toString();
        String fecha = txtFecha.getText().toString();
        String edad = txtEdad.getText().toString();
        String pais = txtPais.getSelectedItem().toString();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();


        Map<String, String> datosJugador = new HashMap<>();

        datosJugador.put("Uid", uidString);
        datosJugador.put("Email", email);
        datosJugador.put("Password", password);
        datosJugador.put("Nombres", nombre);
        datosJugador.put("Fecha", fecha);
        datosJugador.put("Edad", edad);
        datosJugador.put("Pais", pais);
        datosJugador.put("Imagen", "");
        datosJugador.put("Zombies", contador+"");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // NOMBRE BASE DATOS
        DatabaseReference reference = database.getReference(Constantes.NAME_BD);
        // Guarda los datos

        reference.child(uidString).setValue(datosJugador);
        startActivity(new Intent(Registro.this, Menu.class));

        Toast.makeText(Registro.this, "Jugador creado", Toast.LENGTH_LONG).show();

        finish();
    }
}