package com.tapp.bosstimer.Utilidades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.tapp.bosstimer.Clases.mainPlayers;
import com.tapp.bosstimer.R;

import java.util.List;

public class spinnerAdapter extends BaseAdapter
{
    private Context context;

    List<mainPlayers> datos = null;

    public spinnerAdapter(Context context, List<mainPlayers> datos)
    {
        this.context = context;
        this.datos = datos;
    }

    //este método establece el elemento seleccionado sobre el botón del spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_spinner,null);
        }
        ((TextView) convertView.findViewById(R.id.txtDescripcion)).setText(datos.get(position).getNombre());
        ImageView imgAlerta =  convertView.findViewById(R.id.txtImagen);
        Picasso.get().load(datos.get(position).getImagen()).into(imgAlerta);
        return convertView;
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}