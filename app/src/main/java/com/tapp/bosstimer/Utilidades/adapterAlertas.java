package com.tapp.bosstimer.Utilidades;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tapp.bosstimer.Clases.mainAlertas;
import com.tapp.bosstimer.R;

import java.util.List;

public class adapterAlertas extends RecyclerView.Adapter<adapterAlertas.AlertasViewHolder> {
    private List<mainAlertas> mListAlertas;

    // Obtener referencias de los componentes visuales para cada elemento
    // Es decir, referencias de los EditText, TextViews, Buttons
    public static class AlertasViewHolder extends RecyclerView.ViewHolder {
        public TextView nombrePlayer;
        public TextView nombreBoss;
        public TextView tiempoSonara;
        public ImageView imagen;
        public AlertasViewHolder(View view) {
            super(view);
            nombreBoss = view.findViewById(R.id.nombreBoss);
            nombrePlayer = view.findViewById(R.id.nombrePlayer);
            imagen = view.findViewById(R.id.imagen);
            tiempoSonara = view.findViewById(R.id.tiempoRestante);
        }
    }

    public adapterAlertas(List<mainAlertas> myDataSet) {
        mListAlertas = myDataSet;
    }

    @Override
    public AlertasViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // Creamos una nueva vista
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alertas_layout, parent, false);

        // Aquí podemos definir tamaños, márgenes, paddings, etc
        return new AlertasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AlertasViewHolder holder, int position) {
        
    }

    @Override
    public int getItemCount() {
        return mListAlertas.size();
    }
}
