package com.example.juegozombie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.juegozombie.commons.Constantes;
import com.example.juegozombie.commons.Disegno;
import com.example.juegozombie.entities.Jugador;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Menu extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference; // jugadores
    private Button btnJugar;
    private Button btnPuntuacion;
    private Button btnAcercaDe;
    private Button btnCerrarSesion;
    private Button btnEditar;
    private Button btnCambiasPass;


    private TextView txtTitleMenu;
    private TextView txtPerfilMenu;
    private TextView txtFechaMenu;
    private TextView txtEdadMenu;
    private TextView txtPaisMenu;
    private TextView txtZombieMenu;
    private TextView txtUidMenu;
    private TextView txtSubTitleMenu;
    private TextView txtCorreoJugaMenu;
    private TextView txtNombreJugaMenu;
    private Jugador currentPlayer;
    private ImageView imagen;
    private CircleImageView imgPerfil;
    private ImageView imgEditar;

    /* Para cambiar foto perfil*/

    private StorageReference referenciaAlmacenamiento;
    private String rutaAlmecamiento = "FotosDePerfil/*";

    /*Permisos*/
    private static final int CODIGO_SOLICITUD_ALMACENAMIENTO = 200;
    private static final int CODIGO_SELECCION_IMAGEN = 300;

    /*MATRICES*/
    private String[] permisosAlmacenamiento;
    private Uri imagenUri;
    private String perfil; // nombre column


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

    private void initApp() {
        findByWidget();
        setListenerClick();
        setTypeFont();
        initFirebase();
        onloadGif();
    }

    private void initFirebase() {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(Constantes.NAME_BD);

        // Iniciar para cambiar foto perfil
        referenciaAlmacenamiento = FirebaseStorage.getInstance().getReference();
        permisosAlmacenamiento = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    private void findByWidget() {

        txtTitleMenu = findViewById(R.id.txtTitleMenu);
        txtPerfilMenu = findViewById(R.id.txtTitlePerfil);
        txtEdadMenu = findViewById(R.id.txtEdadMenu);
        txtPaisMenu = findViewById(R.id.txtPaisMenu);
        txtFechaMenu = findViewById(R.id.txtFechaMenu);
        txtUidMenu = findViewById(R.id.txtUidMenu);
        txtZombieMenu = findViewById(R.id.txtZombiesMenu);
        txtSubTitleMenu = findViewById(R.id.txtSubTitleMenu);
        txtCorreoJugaMenu = findViewById(R.id.txtCorreoJugadorMenu);
        txtNombreJugaMenu = findViewById(R.id.txtNombreJugadorMenu);
        imgEditar = findViewById(R.id.imgEditar);
        imgPerfil = findViewById(R.id.imgPerfil);

        btnJugar = findViewById(R.id.btnJugar);
        btnPuntuacion = findViewById(R.id.btnPuntaciones);
        btnAcercaDe = findViewById(R.id.btnHacerca);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnEditar = findViewById(R.id.btnEditar);
        btnCambiasPass = findViewById(R.id.btnCambiarPass);
        imagen = findViewById(R.id.imageGif);
    }

    private void setListenerClick() {
        btnPuntuacion.setOnClickListener(this);
        btnAcercaDe.setOnClickListener(this);
        btnJugar.setOnClickListener(this);
        btnCerrarSesion.setOnClickListener(this);
        btnEditar.setOnClickListener(this);
        btnCambiasPass.setOnClickListener(this);
        imgEditar.setOnClickListener(this);
    }

    private void setTypeFont() {

        Typeface typeface = Disegno.getTypeFace(this);

        txtTitleMenu.setTypeface(typeface);
        txtPerfilMenu.setTypeface(typeface);
        txtEdadMenu.setTypeface(typeface);
        txtFechaMenu.setTypeface(typeface);
        txtPaisMenu.setTypeface(typeface);
        txtSubTitleMenu.setTypeface(typeface);
        txtCorreoJugaMenu.setTypeface(typeface);
        txtNombreJugaMenu.setTypeface(typeface);
        txtZombieMenu.setTypeface(typeface);
        btnJugar.setTypeface(typeface);
        btnPuntuacion.setTypeface(typeface);
        btnAcercaDe.setTypeface(typeface);
        btnCerrarSesion.setTypeface(typeface);
        btnEditar.setTypeface(typeface);
        btnCambiasPass.setTypeface(typeface);


    }

    private void onloadGif() {

        //String url = "https://c.tenor.com/FkC4OX_XzowAAAAC/calabaza-pumpkin.gif";
        String url = "https://i.pinimg.com/originals/35/37/56/3537568867d0b27733c47299f1f2e999.gif";
        Uri urlParse = Uri.parse(url);
        Glide.with(getApplicationContext()).load(urlParse).into(imagen);

    }

    private void usuarioLogeado() {

        //  auth.signOut();

        if (user != null) {
            consulta();
            Toast.makeText(this, "en linea", Toast.LENGTH_LONG).show();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == btnJugar.getId()) {
            jugar();

        } else if (id == btnPuntuacion.getId()) {
            startActivity( new Intent(this, Puntajes.class));
        } else if (id == btnAcercaDe.getId()) {
        } else if (id == btnCerrarSesion.getId()) {
            cerrarSesion();
        } else if (id == btnEditar.getId()) {
            editarDatos();
        } else if (id == imgEditar.getId()) {
            actualizarFoto();
        }
    }

    private void editarDatos() {

        String[] opciones = {"Cambiar Edad", "Cambiar Pais"};

        AlertDialog.Builder buider = new AlertDialog.Builder(this);
        buider.setTitle("Editar Datos");
        buider.setItems(opciones, (dialog, index) -> {


            if (index == 0) {
                actualizarEdad();
            } else if (index == 1) {
                actualizarPais();
            }


        }).create().show();


    }

    private void actualizarPais() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualizar Pais");

        LinearLayout linearLayout = new LinearLayout(this);

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(5, 5, 5, 5);


        Spinner spinner = new Spinner(this);

        String paises[] = {"Ecuador", "Colombia", "Peru", "Venezuela", "Argentina"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, paises));
        linearLayout.addView(spinner);
        builder.setView(linearLayout);

        builder.setPositiveButton("Actualizar", (di, index) -> {

            String value = spinner.getSelectedItem().toString();
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("Pais", value);

            databaseReference.child(user.getUid()).updateChildren(mapa)
                    .addOnSuccessListener((unsed) -> {

                        Toast.makeText(Menu.this, "Pais actualizado", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener((exception) -> {

                        Toast.makeText(Menu.this, "Error " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });


        builder.setNegativeButton("Cancelar", (di, index) -> {
        });

        builder.create().show();


    }

    private void actualizarEdad() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualizar Edad");

        LinearLayout linearLayout = new LinearLayout(this);

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(5, 5, 5, 5);

        EditText editText = new EditText(this);
        editText.setHint("Ingrese nueva edad");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        linearLayout.addView(editText);
        builder.setView(linearLayout);

        builder.setPositiveButton("Actualizar", (di, index) -> {

            String value = editText.getText().toString();

            value = value.isEmpty() ? "0" : value;

            Map<String, Object> mapa = new HashMap<>();
            mapa.put("Edad", value);

            databaseReference.child(user.getUid()).updateChildren(mapa)
                    .addOnSuccessListener((unsed) -> {

                        Toast.makeText(Menu.this, "Edad actualizada", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener((exception) -> {

                        Toast.makeText(Menu.this, "Error " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });


        builder.setNegativeButton("Cancelar", (di, index) -> {
        });

        builder.create().show();


    }

    private void actualizarFoto() {

        perfil = "Imagen";
        String[] opciones = {"Galeria"};

        AlertDialog.Builder buider = new AlertDialog.Builder(this);
        buider.setTitle("Selecciona imagen");
        buider.setItems(opciones, new DialogInterface.OnClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {

                // Galeria
                if (index == 0) {

                    if (!comprobarPermisoAlmacenamiento()) {

                        solicitarPermisoAlmacenamiento();

                    } else elegirImagenGaleria();

                }
            }
        });


        buider.create().show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void solicitarPermisoAlmacenamiento() {

        requestPermissions(permisosAlmacenamiento, CODIGO_SOLICITUD_ALMACENAMIENTO);
    }

    /**
     * Verifica si los permisos de almacenamientos estan habilitados o no
     *
     * @return
     */
    private boolean comprobarPermisoAlmacenamiento() {


        boolean resultado = ContextCompat.
                checkSelfPermission(
                        Menu.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return resultado;
    }


    /**
     * Se llama cuando el usuario permite| denega el cuadro de dialogo
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {


            case CODIGO_SOLICITUD_ALMACENAMIENTO: {

                if (grantResults.length > 0) {

                    boolean escrituraAlmacenamientoAcertado = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (escrituraAlmacenamientoAcertado) {

                        // Permiso fue habilitado

                        elegirImagenGaleria();
                    } else
                        Toast.makeText(this, "Habilite Permiso de la Galeria", Toast.LENGTH_SHORT).show();


                }

            }
            break;
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * Se llama cuando el jugador ya ha elegido la imagen
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {

            // De la img vamos obtener la URI
            if (requestCode == CODIGO_SELECCION_IMAGEN) {

                imagenUri = data.getData();

                subirFoto(imagenUri);
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Cambia la foto de perfil y actualizado en la base de datos
     *
     * @param imagenUri
     */
    private void subirFoto(Uri imagenUri) {


        String rutaArchivoNombre = rutaAlmecamiento + "" + perfil + "" + user.getUid();

        StorageReference storageReference = referenciaAlmacenamiento.child(rutaArchivoNombre);
        storageReference.putFile(imagenUri).addOnSuccessListener(taskSnapshot -> {

            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;

            Uri dowloadUri = uriTask.getResult();
            if (uriTask.isSuccessful()) {

                Map<String, Object> resultado = new HashMap<>();
                resultado.put(perfil, dowloadUri.toString());

                databaseReference.child(user.getUid()).updateChildren(resultado)
                        .addOnSuccessListener((unsed) -> {

                            Toast.makeText(Menu.this, "Imagen perfil actualizada con exito", Toast.LENGTH_SHORT).show();

                        }).addOnFailureListener((exception) -> {

                            Toast.makeText(Menu.this, "Ha ocurrido un error " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else Toast.makeText(Menu.this, "Algo a salido mal", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener((exception) -> {

            Toast.makeText(Menu.this, "Algo a ido mal " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }

    /**
     * Abre la galeria
     */
    private void elegirImagenGaleria() {

        Intent intentGaleria = new Intent(Intent.ACTION_PICK);
        intentGaleria.setType("image/*");
        startActivityForResult(intentGaleria, CODIGO_SELECCION_IMAGEN);
    }

    private void cerrarSesion() {
        auth.signOut();
        startActivity(new Intent(Menu.this, MainActivity.class));
        Toast.makeText(this, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show();

    }

    private void consulta() {
        Query query = databaseReference.orderByChild("Email").equalTo(user.getEmail());

        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot ds = snapshot.getChildren().iterator().next();

                currentPlayer = new Jugador();

                currentPlayer.setuId(ds.child("Uid").getValue().toString());
                currentPlayer.setEdad(ds.child("Edad").getValue().toString());
                currentPlayer.setPais(ds.child("Pais").getValue().toString());
                currentPlayer.setFecha(ds.child("Fecha").getValue().toString());
                currentPlayer.setPuntaje(Integer.parseInt(ds.child("Zombies").getValue().toString()));
                currentPlayer.setNombres(ds.child("Nombres").getValue().toString());
                currentPlayer.setEmail(ds.child("Email").getValue().toString());
                currentPlayer.setImagen(ds.child("Imagen").getValue().toString());

                txtUidMenu.setText(currentPlayer.getuId());
                // Obliatorio convertir en String
                txtZombieMenu.setText("" + currentPlayer.getPuntaje());
                txtCorreoJugaMenu.setText(currentPlayer.getEmail());
                txtNombreJugaMenu.setText(currentPlayer.getNombres());
                txtEdadMenu.setText(currentPlayer.getEdad() + "");
                txtPaisMenu.setText(currentPlayer.getPais());
                txtFechaMenu.setText(currentPlayer.getFecha());

                if (!currentPlayer.getImagen().equals("")) {
                    Picasso.get().load(currentPlayer.getImagen()).into(imgPerfil);
                } else Picasso.get().load(R.drawable.default_perfil).into(imgPerfil);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void jugar() {

        Intent intent = new Intent(this, EscenarioJuego.class);
        intent.putExtra("jugadorActual", currentPlayer);
        startActivity(intent);
    }
}