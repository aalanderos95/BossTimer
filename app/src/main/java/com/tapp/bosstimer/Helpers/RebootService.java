package com.tapp.bosstimer.Helpers;

import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.tapp.bosstimer.Clases.mainAlertas;
import com.tapp.bosstimer.MainActivity;
import com.tapp.bosstimer.R;
import com.tapp.bosstimer.SqliteDbHelper;
import com.tapp.bosstimer.Utilidades.Utilidades;
import com.tapp.bosstimer.Utilidades.Utils;

import java.util.Calendar;

public class RebootService extends IntentService {

    public RebootService(String name)
    {
        super(name);
        startForeground(1, new Notification());
    }

    public RebootService()
    {
        super("RebootService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String intentType = intent.getExtras().getString("caller");
        if(intentType == null) return;
        if(intentType.equals("RebootReceiver"))
        {
            SharedPreferences settings = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
            SqliteDbHelper cnn = new SqliteDbHelper(this, Utilidades.DB,null,1);
            SQLiteDatabase db = cnn.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT NOTI."+ Utilidades.ID +", P."+ Utilidades.Nombre+ ", NOTI."+Utilidades.BossID +
                    ", NOTI."+Utilidades.Imagen+ ", NOTI."+ Utilidades.Hour+", NOTI."+Utilidades.Min + ", NOTI." +Utilidades.timeSpan +
                    ", NOTI."+Utilidades.HourMonster+
                    " FROM "+Utilidades.TABLA_NOTIFICACIONES +" AS NOTI" +
                    " INNER JOIN "+ Utilidades.TABLA_PLAYERS +" AS P ON P."+ Utilidades.ID + " = NOTI." + Utilidades.PlayerID                 , null);
            if (c.moveToFirst()){
                do {
                    // Passing values
                    int id = c.getInt(0);
                    String nombrePlayer = c.getString(1);
                    String NombreBoss = c.getString(2);
                    String ImagenBoss = c.getString(3);
                    String Hour = c.getString(4);
                    String Min = c.getString(5);
                    String timeSpan = c.getString(6);
                    String HourMonsters = c.getString(7);

                    String[] splitFechaCompleta = timeSpan.split(" ");
                    String[] splitHora = splitFechaCompleta[1].split(":");
                    String[] splitFecha = splitFechaCompleta[0].split("-");

                    Calendar calendarDefinido = Calendar.getInstance();
                    calendarDefinido.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitFecha[0]));
                    calendarDefinido.set(Calendar.MONTH, Integer.parseInt(splitFecha[1])-1);
                    calendarDefinido.set(Calendar.YEAR, Integer.parseInt(splitFecha[2]));

                    calendarDefinido.set(Calendar.HOUR, Integer.parseInt(splitHora[0]));
                    calendarDefinido.set(Calendar.MINUTE, Integer.parseInt(splitHora[1]));
                    calendarDefinido.set(Calendar.SECOND, Integer.parseInt(splitHora[2]));
                    Utils.setAlarm(Integer.parseInt(String.valueOf(id)), calendarDefinido.getTimeInMillis(), this,
                            nombrePlayer,NombreBoss);

                    // Do something Here with values
                } while(c.moveToNext());
            }
            c.close();
            db.close();


        }
    }
}
