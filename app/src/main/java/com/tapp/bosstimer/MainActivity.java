package com.tapp.bosstimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tapp.bosstimer.Helpers.AlarmReceiver;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private String boss;
    private String characterName ;
    private MediaPlayer player;
    private AlertDialog modalInformacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundles = getIntent().getExtras();
        SqliteDbHelper cnn = new SqliteDbHelper(this,"bosstimer",null,1);
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
}