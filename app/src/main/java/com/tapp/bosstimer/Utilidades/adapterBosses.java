package com.tapp.bosstimer.Utilidades;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tapp.bosstimer.BuildConfig;
import com.tapp.bosstimer.R;

import java.util.ArrayList;
import java.util.List;

public class adapterBosses extends RecyclerView.Adapter<adapterBosses.BossViewHolder>
        implements View.OnClickListener{
    private View.OnClickListener listener;
    private List<apiBosses> items;

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onClick(View v) {
        if(listener!= null)
        {
            listener.onClick(v);
        }
    }

    public static class BossViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre;
        public TextView visitas;

        public BossViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombre);
            visitas = (TextView) v.findViewById(R.id.visitas);
        }
    }

    public adapterBosses(List<apiBosses> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public adapterBosses.BossViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.boss_layout, viewGroup, false);
        v.setOnClickListener(this);
        return new BossViewHolder(v);
    }

    @Override
    public void onBindViewHolder(adapterBosses.BossViewHolder viewHolder, int i) {
        //viewHolder.imagen.setImageResource(items.get(i).getImagen());
        viewHolder.nombre.setText(items.get(i).getName());
        viewHolder.visitas.setText(String.valueOf(items.get(i).getHours()));
        //viewHolder.imagen.setImageURI(Uri.parse(items.get(i).getImagen()));
        String urlFinal = "";
        urlFinal = BuildConfig.SERVER_URL+"Bosses/"+items.get(i).name.toLowerCase().replace(" ","_") + ".gif";
        //Rect rect = new Rect(viewHolder.imagen.getLeft(),viewHolder.imagen.getTop(),viewHolder.imagen.getRight(),viewHolder.imagen.getBottom());
        if(urlFinal != "") {
            Picasso.get().load(urlFinal).into(viewHolder.imagen);
        }
    }
}