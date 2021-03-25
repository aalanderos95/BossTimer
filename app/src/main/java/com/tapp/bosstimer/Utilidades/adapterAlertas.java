package com.tapp.bosstimer.Utilidades;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tapp.bosstimer.Clases.mainAlertas;
import com.tapp.bosstimer.MainActivity;
import com.tapp.bosstimer.R;
import com.tapp.bosstimer.SqliteDbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class adapterAlertas extends RecyclerView.Adapter<adapterAlertas.AlertasViewHolder>
    implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<mainAlertas> mListAlertas;
    private Context contexto;

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
    // Obtener referencias de los componentes visuales para cada elemento
    // Es decir, referencias de los EditText, TextViews, Buttons
    public static class AlertasViewHolder extends RecyclerView.ViewHolder {
        public TextView nombrePlayer;
        public TextView nombreBoss;
        public TextView tiempoSonara;
        public ImageView imagen;
        public LinearLayout mainLayout;
        public TextView opciones;
        public AlertasViewHolder(View view) {
            super(view);
            nombreBoss = view.findViewById(R.id.nombreBoss);
            nombrePlayer = view.findViewById(R.id.nombrePlayer);
            imagen = view.findViewById(R.id.imagen);
            tiempoSonara = view.findViewById(R.id.tiempoRestante);
            mainLayout = view.findViewById(R.id.mainLayout);
            opciones = view.findViewById(R.id.textViewOptions);
        }
    }

    public adapterAlertas(Context contexto, List<mainAlertas> myDataSet) {
        this.contexto = contexto;
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
        holder.nombreBoss.setText("BOSS: " +mListAlertas.get(position).getBoss());
        holder.nombrePlayer.setText("PLAYER: " +mListAlertas.get(position).getPlayer());
        //viewHolder.imagen.setImageURI(Uri.parse(items.get(i).getImagen()));
        String urlFinal = mListAlertas.get(position).getImagen();
        Log.e("SERVICIO","imagen:" + urlFinal);
        //Rect rect = new Rect(viewHolder.imagen.getLeft(),viewHolder.imagen.getTop(),viewHolder.imagen.getRight(),viewHolder.imagen.getBottom());
        if(urlFinal != "") {
            Picasso.get().load(urlFinal).into(holder.imagen);
        }
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        Calendar calendarActual = Calendar.getInstance();
        calendarActual.set(Calendar.HOUR, today.hour);
        calendarActual.set(Calendar.MINUTE, today.minute);
        Calendar calendarDefinido = Calendar.getInstance();

        //calendarDefinido.setTimeInMillis(mListAlertas.get(position).getTimeSpan());
        Log.e("SERVICIO", mListAlertas.get(position).getTimeSpan());
        String[] splitFechaCompleta = mListAlertas.get(position).getTimeSpan().split(" ");
        String[] splitHora = splitFechaCompleta[1].split(":");
        String[] splitFecha = splitFechaCompleta[0].split("-");

        calendarDefinido.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitFecha[0]));
        calendarDefinido.set(Calendar.MONTH, Integer.parseInt(splitFecha[1])-1);
        calendarDefinido.set(Calendar.YEAR, Integer.parseInt(splitFecha[2]));

        calendarDefinido.set(Calendar.HOUR, Integer.parseInt(splitHora[0]));
        calendarDefinido.set(Calendar.MINUTE, Integer.parseInt(splitHora[1]));
        calendarDefinido.set(Calendar.SECOND, Integer.parseInt(splitHora[2]));
        //calendarDefinido.setTimeZone(TimeZone.getTimeZone(splitFechaCompleta[2]));
        String fechaDefinida = splitFechaCompleta[0]+ " " + splitFechaCompleta[1];
        /*String fechaDefinida = (dia.length() == 1 ? "0"+calendarDefinido.get(Calendar.DAY_OF_MONTH) : dia)
                + "/"+ (mes.length() == 1 ? "0" + (calendarDefinido.get(Calendar.MONTH)+1) : mes)+"/"
                + calendarDefinido.get(Calendar.YEAR) + " "
                + (hora.length() == 1 ? "0"+calendarDefinido.get(Calendar.HOUR) : hora) +
                ":"
                + (min.length() == 1 ? "0"+calendarDefinido.get(Calendar.MINUTE) : min)
                + " " + (calendarDefinido.get(Calendar.AM_PM) == 1 ? "PM" : "AM");*/
        holder.tiempoSonara.setText("Fecha: " + fechaDefinida);

        if(calendarActual.getTime().compareTo(calendarDefinido.getTime()) > 0 )
        {
            holder.mainLayout.setBackgroundColor(this.contexto.getResources().getColor(R.color.green));
            holder.opciones.setVisibility(View.VISIBLE);
        }
        else {
            holder.mainLayout.setBackgroundColor(this.contexto.getResources().getColor(R.color.red));
            holder.opciones.setVisibility(View.GONE);
        }


        holder.opciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(contexto, holder.opciones);
                popup.inflate(R.menu.menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_delete:
                                SqliteDbHelper cnn = new SqliteDbHelper(contexto, Utilidades.DB,null,1);
                                SQLiteDatabase db = cnn.getReadableDatabase();
                                Time today = new Time(Time.getCurrentTimezone());
                                today.setToNow();
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, today.hour);
                                calendar.set(Calendar.MINUTE, today.minute);
                                calendar.add(Calendar.HOUR,today.HOUR);
                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                String newTime = df.format(calendar.getTime());

                                ContentValues values = new ContentValues();
                                values.put(Utilidades.Hour, today.HOUR);
                                values.put(Utilidades.Min, today.MINUTE);
                                values.put(Utilidades.timeSpan, newTime);
                                db.update(Utilidades.TABLA_NOTIFICACIONES,values,
                                        Utilidades.ID + "="+mListAlertas.get(position).getID(),
                                        null);
                                db.close();
                                notifyDataSetChanged();
                                break;
                        }
                        return false;
                    }
                });

                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListAlertas.size();
    }
}

