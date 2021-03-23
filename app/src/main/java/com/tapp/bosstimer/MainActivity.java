package com.tapp.bosstimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tapp.bosstimer.Clases.mainAlertas;
import com.tapp.bosstimer.Helpers.AlarmReceiver;
import com.tapp.bosstimer.Utilidades.Servicios;
import com.tapp.bosstimer.Utilidades.Utilidades;
import com.tapp.bosstimer.Utilidades.apiBoss;
import com.tapp.bosstimer.Utilidades.apiBosses;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private String boss;
    private String characterName ;
    private MediaPlayer player;
    private AlertDialog modalInformacion;
    private Servicios service;
    private Call<apiBoss> requestBosses;
    private Retrofit retro;

    private List<apiBoss> bossesList;
    private List<mainAlertas> alertasList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundles = getIntent().getExtras();
        CargarAdapter cargarAdapter = new CargarAdapter(service,requestBosses,retro);
        cargarAdapter.execute();
        SqliteDbHelper cnn = new SqliteDbHelper(this, Utilidades.DB,null,1);
        Log.d("SERVICIO","bundles" + bundles);
        if(bundles != null) {
            Log.d("SERVICIO","bundles");
            String opcion = bundles.getString("ACCION", "");
            if (!opcion.isEmpty()) {
                boss = bundles.getString("BOSS", "");
                characterName = bundles.getString("CHAR", "");

                String descripcion = "Ya puedes matarlo con tu character: " + characterName;
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                if (alarmSound == null) {
                    alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    if(alarmSound == null)
                    {
                        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    }
                }
                player = MediaPlayer.create(this, alarmSound);

                AudioManager audioManager= (AudioManager) getSystemService(AUDIO_SERVICE);
                int volumeLevel=audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
                player.setVolume(volumeLevel,volumeLevel);
                player.start();
                abrirInformacion(boss, descripcion);
            }
        }
        alertasList = new ArrayList<>();
        obtenerAlertas();

    }

    private void obtenerAlertas() {
        SqliteDbHelper cnn = new SqliteDbHelper(this, Utilidades.DB,null,1);
        SQLiteDatabase db = cnn.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT NOTI."+ Utilidades.ID +", P."+ Utilidades.Nombre+ ", B."+ Utilidades.Nombre +" " +
                ", NOTI."+Utilidades.Imagen+ ", NOTI."+ Utilidades.Hour+", NOTI."+Utilidades.Min +
                "FROM "+Utilidades.TABLA_NOTIFICACIONES +"AS NOTI" +
                " INNER JOIN "+ Utilidades.TABLA_PLAYERS +" AS P ON P."+ Utilidades.ID + " = NOTI." + Utilidades.PlayerID
                +" INNER JOIN "+ Utilidades.TABLA_BOSS + " AS B ON B."+ Utilidades.ID +" = NOTI."+ Utilidades.BossID                , null);
        if (c.moveToFirst()){
            do {
                // Passing values
                int id = c.getInt(0);
                String nombrePlayer = c.getString(1);
                String NombreBoss = c.getString(2);
                String ImagenBoss = c.getString(3);
                String Hour = c.getString(4);
                String Min = c.getString(5);
                alertasList.add(new mainAlertas(id,nombrePlayer, NombreBoss,  Hour, Min,ImagenBoss));
                // Do something Here with values
            } while(c.moveToNext());
        }
        c.close();
        db.close();
    }

    private void llenarAdapter(){

    }
    private void agregarAlerta(int idBoss, int idPlayer, String hour, String min)
    {
        SqliteDbHelper cnn = new SqliteDbHelper(this, Utilidades.DB,null,1);
        SQLiteDatabase db = cnn.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Utilidades.BossID, idBoss);
        values.put(Utilidades.PlayerID, idPlayer);
        values.put(Utilidades.Hour, hour);
        values.put(Utilidades.Min, min);

        Long id = db.insert(Utilidades.TABLA_NOTIFICACIONES, Utilidades.ID, values);
    }

    private void obtenerInformacion()
    {

    }
    private void abrirInformacion(String title, String descripcion) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mview = getLayoutInflater().inflate(R.layout.informacion_layout, null);
        //Obtener Objetos Jugadas.
        Button btnCerrar = mview.findViewById(R.id.btnCerrar);
        TextView txtTitulo = mview.findViewById(R.id.txtTitle);
        TextView txtDescripcion = mview.findViewById(R.id.txtDescripcion);
        txtTitulo.setText("BOSS:"+title);
        txtDescripcion.setText(descripcion);

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
                modalInformacion.dismiss();
            }
        });
        mBuilder.setView(mview);
        modalInformacion = mBuilder.create();
        // modalRating.setCancelable(false);
        if(modalInformacion != null) {
            modalInformacion.show();
        }
    }





    //ASYNC

    private class CargarAdapter extends AsyncTask<Void,Void,Void> {

        private Servicios service;
        private Call<apiBoss> requestBosses;
        private Retrofit retro;
        private CargarAdapter(Servicios service, Call<apiBoss> requestBosses, Retrofit retro)
        {
            this.service = service;
            this.requestBosses = requestBosses;
            this.retro = retro;
        }
        @Override
        protected void onPreExecute() {
            retro = new Retrofit.Builder()
                    .baseUrl(BuildConfig.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retro.create(Servicios.class);
            String uuid = Settings.Secure.getString(MainActivity.this.getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
            requestBosses = service.listaBoss("bosses.php");//"1".toString());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            retro = null;
            service = null;
            requestBosses = null;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            requestBosses.enqueue(new Callback<apiBoss>() {
                @Override
                public void onResponse(Call<apiBoss> call, Response<apiBoss> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                        //recycler.setVisibility(View.VISIBLE);
                    } else {

                        apiBoss anime = response.body();
                        //Toast.makeText(getActivity(), ""+anime.fEstado, Toast.LENGTH_SHORT).show();
                        //if(anime == 1) {
                            apiBoss mainApiBoss = new apiBoss();
                            mainApiBoss.name = anime.name;
                            mainApiBoss.bosses = new ArrayList<>();
                            for (apiBosses c : anime.bosses) {
                                mainApiBoss.bosses.add(c);
                            }

                        //}
                        //else
                        //{
                        //    Toast.makeText(vistaAnime.this, getResources().getString(R.string.sininformacion), Toast.LENGTH_SHORT).show();
                        //}
                        //Utilidades.animeview = true;
                    }
                }
                @Override
                public void onFailure(Call<apiBoss> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "BossTimer Error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    //recycler.setVisibility(View.VISIBLE);
                }
            });
            return null;
        }
    }
}