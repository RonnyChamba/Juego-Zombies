package com.example.juegozombie.commons;

import android.content.Context;
import android.graphics.Typeface;

public class Disegno {

    public static Typeface getTypeFace(Context context){
        return   Typeface.createFromAsset(context.getAssets(), Constantes.PATH_FUENTE);
    }
}
