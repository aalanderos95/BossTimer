package com.tapp.bosstimer.Utilidades;

import android.util.Log;
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
    public void onBindViewHolder(adapterAlertas.AlertasViewHolder holder, int position) {
        //viewHolder.imagen.setImageResource(items.get(i).getImagen());
        holder.nombreBoss.setText(mListAlertas.get(position).getBoss());
        holder.nombrePlayer.setText(mListAlertas.get(position).getPlayer());
        //viewHolder.imagen.setImageURI(Uri.parse(items.get(i).getImagen()));
        String urlFinal = mListAlertas.get(position).getImagen();
        Log.e("SERVICIO","imagen:" + urlFinal);
        //Rect rect = new Rect(viewHolder.imagen.getLeft(),viewHolder.imagen.getTop(),viewHolder.imagen.getRight(),viewHolder.imagen.getBottom());
        if(urlFinal != "") {
            Picasso.get().load(urlFinal).into(holder.imagen);
        }
        holder.tiempoSonara.setText("Definida a las "+mListAlertas.get(position).getHour()+":"+mListAlertas.get(position).getMin());
    }

    @Override
    public int getItemCount() {
        return mListAlertas.size();
    }
}
