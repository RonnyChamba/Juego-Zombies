package com.example.juegozombie.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.juegozombie.R;

public class DialogFragmentAcerca  extends androidx.fragment.app.DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater.inflate(), container, savedInstanceState);
        View  view = inflater.inflate(R.layout.acercade, container, false);

        Button btnOk = view.findViewById(R.id.btnAcercaoK);
        btnOk.setOnClickListener( (event) ->this.dismiss());

        return  view;
    }
}
