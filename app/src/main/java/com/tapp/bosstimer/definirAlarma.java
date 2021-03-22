package com.tapp.bosstimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

public class definirAlarma extends AppCompatActivity {
    AlarmManager alarmManager;
    private Button alarmToggle;
    private TimePicker alarmTimePicker;
    private int AlarmID = 1;
    private String boss;
    private String characterName ;
    private TextView alarmTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definir_alarma);

        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmTextView = (TextView) findViewById(R.id.alarmText);
        alarmToggle = (Button) findViewById(R.id.alarmToggle);
        alarmToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                    calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
                    int hora = alarmTimePicker.getCurrentHour();
                    int minutos = alarmTimePicker.getCurrentMinute();
                    String hora_programada = new StringBuilder().append(hora).append(":0").append(minutos) + " hrs";
                    if(minutos < 10)
                    {
                        setAlarmText("Tarea programada a las --> " + hora_programada);
                    }else {
                        hora_programada = new StringBuilder().append(hora).append(":").append(minutos) + " hrs";
                        setAlarmText("Tarea programada a las --> " + hora_programada);
                    }


                    Toast.makeText(getApplicationContext(), "Tarea programada a las " + hora_programada.toString(), Toast.LENGTH_SHORT).show();
                    setAlarm(AlarmID, calendar.getTimeInMillis(),definirAlarma.this);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },2000);

                }catch(Exception ex) {
                    Log.d("EVENTOS","Error al programar hora de tarea: " + ex.getMessage());
                }

            }
        });
    }


    private void setAlarm(int i, long timestamp, Context ctx){
        //PRUEBA
        characterName = "ACRORED";
        boss = "PUTITO";

        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(ctx, AlarmReceiver.class);
        alarmIntent.putExtra("CHAR",characterName);
        alarmIntent.putExtra("BOSS",boss);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(ctx, i, alarmIntent, PendingIntent.FLAG_ONE_SHOT);

        alarmIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp,pendingIntent);

    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }
}