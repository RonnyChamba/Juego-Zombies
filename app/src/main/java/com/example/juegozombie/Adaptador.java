package com.example.juegozombie;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.juegozombie.commons.Disegno;
import com.example.juegozombie.entities.Jugador;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptador extends  RecyclerView.Adapter<Adaptador.MyHolder>{

    private Context context;
    private List<Jugador> jugadorList;

    public Adaptador(Context context, List<Jugador> jugadorList){
        this.context = context;
        this.jugadorList = jugadorList;
    }

    /**
     * Inflamos el diseño
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(context).inflate(R.layout.jugadores, parent, false);
        return  new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String  imgPerfil = jugadorList.get(position).getImagen();
        String   nombre = jugadorList.get(position).getNombres();
        String  zombies = jugadorList.get(position).getPuntaje() +"";
        String  email = jugadorList.get(position).getEmail();
        String  pais = jugadorList.get(position).getPais();

        holder.txtNombreJugador.setText(nombre);
        holder.txtCorreoJugador.setText(email);
        holder.txtPuntajeJugador.setText(zombies);
        int codigoImg = 0;

        switch (pais) {

            case "Ecuador" : codigoImg = R.drawable.flag_ecuador;
                break;
            case "Colombia" : codigoImg = R.drawable.flag_colombia;
                break;

            case "Peru" : codigoImg = R.drawable.flag_peru;
                break;


            case "Venezuela" : codigoImg = R.drawable.flag_venezuela;
                break;

            case "Argentina" : codigoImg = R.drawable.flag_argentina;
                break;

            default:
                codigoImg = R.drawable.zombie_muerto;
                break;
        }

        //holder.imgPaisJugador.setImageResource(codigoImg);
        Picasso.get().load(codigoImg).into(holder.imgPaisJugador);

        if (!imgPerfil.equals("")){
            Picasso.get().load(imgPerfil).into(holder.imagenJugador);
        }else Picasso.get().load(R.drawable.default_perfil).into(holder.imagenJugador);




    }

    @Override
    public int getItemCount() {
        return jugadorList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        CircleImageView imagenJugador;
        TextView txtNombreJugador;
        TextView txtCorreoJugador;
        TextView txtPuntajeJugador;
        CircleImageView imgPaisJugador;
        View itemView;

        public  MyHolder(@NonNull View itemView){
            super(itemView);

            this.itemView = itemView;
            findByObjects();
            setFont();
        }
        private  void  findByObjects(){

            imagenJugador = itemView.findViewById(R.id.imgJugador);
            txtNombreJugador = itemView.findViewById(R.id.txtNombreJugador);
            txtCorreoJugador = itemView.findViewById(R.id.txtCorreoJugador);
            txtPuntajeJugador = itemView.findViewById(R.id.txtPuntajeJugador);
            imgPaisJugador = itemView.findViewById(R.id.imgPaisJugador);
        }

        private void setFont(){
            Typeface typeface = Disegno.getTypeFace(itemView.getContext());

            txtNombreJugador.setTypeface(typeface);
            txtCorreoJugador.setTypeface(typeface);
            txtPuntajeJugador.setTypeface(typeface);
        }

    }
}
