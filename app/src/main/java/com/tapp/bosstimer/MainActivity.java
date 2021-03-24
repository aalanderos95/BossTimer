package com.tapp.bosstimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tapp.bosstimer.Clases.mainAlertas;
import com.tapp.bosstimer.Clases.mainPlayers;
import com.tapp.bosstimer.Helpers.AlarmReceiver;
import com.tapp.bosstimer.Utilidades.Servicios;
import com.tapp.bosstimer.Utilidades.Utilidades;
import com.tapp.bosstimer.Utilidades.adapterBosses;
import com.tapp.bosstimer.Utilidades.apiBoss;
import com.tapp.bosstimer.Utilidades.apiBosses;
import com.tapp.bosstimer.Utilidades.spinnerAdapter;

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
    private AlertDialog modalPlayers;
    private AlertDialog modalBoss;
    private AlertDialog modalAlertas;
    private Servicios service;
    private Call<apiBoss> requestBosses;
    private Retrofit retro;
    private List<mainPlayers> listaPlayers;
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
        listaPlayers = new ArrayList<>();
        alertasList = new ArrayList<>();
        obtenerAlertas();
        recargarListaPlayers();

        ImageView btnAgregarAlerta = findViewById(R.id.agregarNuevaNotificacion);
        btnAgregarAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeleccionaBoss();
            }
        });
    }

    private void obtenerAlertas() {
        SqliteDbHelper cnn = new SqliteDbHelper(this, Utilidades.DB,null,1);
        SQLiteDatabase db = cnn.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT NOTI."+ Utilidades.ID +", P."+ Utilidades.Nombre+ ", B."+ Utilidades.Nombre +" " +
                ", NOTI."+Utilidades.Imagen+ ", NOTI."+ Utilidades.Hour+", NOTI."+Utilidades.Min +
                " FROM "+Utilidades.TABLA_NOTIFICACIONES +" AS NOTI" +
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

    private void AgregarPlayer()
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mview = getLayoutInflater().inflate(R.layout.agregar_layout, null);
        //Obtener Objetos Jugadas.
        LinearLayout lyPlayers = mview.findViewById(R.id.lyPlayers);
        lyPlayers.setVisibility(View.VISIBLE);
        Button btnGuardar = mview.findViewById(R.id.btnGuardar);
        TextView txtTitulo = mview.findViewById(R.id.txtTitle);
        EditText editNombre = mview.findViewById(R.id.etPlayerName);
        txtTitulo.setText("Agregar Nuevo Player");

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //REGISTRAR NUEVO PLAYER
                SqliteDbHelper cnn = new SqliteDbHelper(MainActivity.this, Utilidades.DB,null,1);
                SQLiteDatabase db = cnn.getReadableDatabase();

                ContentValues values = new ContentValues();
                values.put(Utilidades.Nombre, editNombre.getText().toString());

                Long id = db.insert(Utilidades.TABLA_PLAYERS, Utilidades.ID, values);
                db.close();
                recargarListaPlayers();
                modalPlayers.dismiss();
            }
        });
        mBuilder.setView(mview);
        modalPlayers = mBuilder.create();
        // modalRating.setCancelable(false);
        if(modalPlayers != null) {
            modalPlayers.show();
        }
    }

    private void SeleccionaBoss(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mview = getLayoutInflater().inflate(R.layout.agregar_layout, null);
        //Obtener Objetos Jugadas.
        LinearLayout lyBosses = mview.findViewById(R.id.lyBosses);
        lyBosses.setVisibility(View.VISIBLE);
        Button btnGuardar = mview.findViewById(R.id.btnGuardar);
        btnGuardar.setVisibility(View.GONE);
        TextView txtTitulo = mview.findViewById(R.id.txtTitle);
        txtTitulo.setText("Selecciona un Boss");

        for(apiBoss boss : bossesList)
        {
            TextView txtName = new TextView(this);
            txtName.setText(boss.name);
            txtName.setTextColor(Color.BLACK);
            txtName.setTextSize(20);

            RecyclerView recycler = new RecyclerView(this);
            recycler.setHasFixedSize(true);
            //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recycler.setLayoutManager(new GridLayoutManager(this,3));
            adapterBosses adapter = new adapterBosses(boss.bosses);
            recycler.setClickable(true);
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Seleccionamos: "+boss.bosses.get(recycler.getChildAdapterPosition(v)).getName(),
                            Toast.LENGTH_SHORT).show();
                    AgregarAlertas(boss.bosses.get(recycler.getChildAdapterPosition(v)).getName(),
                            boss.bosses.get(recycler.getChildAdapterPosition(v)).getHours());
                }
            });
            recycler.setAdapter(adapter);

            lyBosses.addView(txtName);
            lyBosses.addView(recycler);
        }


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*//REGISTRAR NUEVO PLAYER
                SqliteDbHelper cnn = new SqliteDbHelper(MainActivity.this, Utilidades.DB,null,1);
                SQLiteDatabase db = cnn.getReadableDatabase();

                ContentValues values = new ContentValues();
                values.put(Utilidades.Nombre, editNombre.getText().toString());

                Long id = db.insert(Utilidades.TABLA_PLAYERS, Utilidades.ID, values);
                db.close();
                recargarListaPlayers();
                modalPlayers.dismiss();*/
            }
        });
        mBuilder.setView(mview);
        modalBoss = mBuilder.create();
        // modalRating.setCancelable(false);
        if(modalBoss != null) {
            modalBoss.show();
        }
    }
    private void AgregarAlertas(String bossName, int hours)
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mview = getLayoutInflater().inflate(R.layout.agregar_layout, null);
        //Obtener Objetos Jugadas.
        LinearLayout lyAlertas = mview.findViewById(R.id.lyAlertas);
        lyAlertas.setVisibility(View.VISIBLE);
        ImageView btnCerrar = mview.findViewById(R.id.btnCerrar);
        btnCerrar.setVisibility(View.VISIBLE);
        Button btnGuardar = mview.findViewById(R.id.btnGuardar);
        TextView txtTitulo = mview.findViewById(R.id.txtTitle);
        TextView txtBossSeleccionado = mview.findViewById(R.id.txtBossSeleccionado);
        txtBossSeleccionado.setText(bossName);
        Spinner spinPlayer = mview.findViewById(R.id.spinPlayers);
        txtTitulo.setText("Agregar Nueva Alerta");

        spinnerAdapter adapterPlayer = new spinnerAdapter(MainActivity.this, listaPlayers);
        spinPlayer.setAdapter(adapterPlayer);
        final int[] PlayerID = {0};
        spinPlayer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PlayerID[0] = listaPlayers.get(position).getID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modalAlertas.dismiss();
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //REGISTRAR NUEVA ALERTA
                String urlFinal = "";
                urlFinal = BuildConfig.SERVER_URL+"Bosses/"+bossName.replace(" ","_") + ".gif";

                SqliteDbHelper cnn = new SqliteDbHelper(MainActivity.this, Utilidades.DB,null,1);
                SQLiteDatabase db = cnn.getReadableDatabase();
                Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, today.hour);
                calendar.set(Calendar.MINUTE, today.minute);
                int hora = today.hour;
                int minutos = today.minute;
                String hora_programada = new StringBuilder().append(hora).append(":0").append(minutos) + " hrs";
                Toast.makeText(getApplicationContext(), "Tarea programada a las " + hora_programada.toString(), Toast.LENGTH_SHORT).show();
                ContentValues values = new ContentValues();
                values.put(Utilidades.BossID, bossName);
                values.put(Utilidades.PlayerID, PlayerID[0]);
                values.put(Utilidades.Hour, hora);
                values.put(Utilidades.Min, minutos);
                values.put(Utilidades.Imagen, urlFinal);

                Long id = db.insert(Utilidades.TABLA_NOTIFICACIONES, Utilidades.ID, values);
                db.close();
                recargarListaPlayers();
                modalPlayers.dismiss();

                /*setAlarm(id, calendar.getTimeInMillis(),MainActivity.this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },2000);

                /**/
            }
        });
        mBuilder.setView(mview);
        modalAlertas = mBuilder.create();
        modalAlertas.setCancelable(false);
        // modalRating.setCancelable(false);
        if(modalAlertas != null) {
            modalAlertas.show();
        }
    }

    private void recargarListaPlayers()
    {
        listaPlayers = new ArrayList<>();
        SqliteDbHelper cnn = new SqliteDbHelper(this, Utilidades.DB,null,1);
        SQLiteDatabase db = cnn.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT ID, NOMBRE FROM "+Utilidades.TABLA_PLAYERS, null);
        if (c.moveToFirst()){
            do {
                // Passing values
                int id = c.getInt(0);
                String nombre = c.getString(1);
                listaPlayers.add(new mainPlayers(id,nombre,BuildConfig.SERVER_URL+"Tibia_icon.png"));
                // Do something Here with values
            } while(c.moveToNext());
        }
        c.close();
        db.close();


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
            bossesList = new ArrayList<>();
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

                        bossesList.add(mainApiBoss);

                        Log.d("SERVICIO", "LISTA PLAYER :" +bossesList.get(0).getName());
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