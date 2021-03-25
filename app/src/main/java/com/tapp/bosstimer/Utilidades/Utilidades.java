package com.tapp.bosstimer.Utilidades;

import android.content.Context;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;

import static android.content.Context.AUDIO_SERVICE;

public class Utilidades {
    public static MediaPlayer player;
    public static String DB = "BOSS";
    public static String ID = "ID";
    public static String TABLA_PLAYERS = "PLAYERS";
    public static String TABLA_BOSS = "BOSS";
    public static String TABLA_NOTIFICACIONES = "NOTIFICACIONES";
    public static String PlayerID = "ID_PLAYER";
    public static String Nombre = "NOMBRE";
    public static String Imagen = "IMAGEN";
    public static String BossID = "BOSS_ID";
    public static String Categoria = "CAT";
    public static String TIMEHOUR = "TIME";
    public static String Hour = "HOUR";
    public static String Min = "MIN";
    public static String timeSpan = "TIME_SPAN";
    public static String HourMonster = "HOUR_MONSTER";

    public static String CREATE_TABLE_PLAYERS ="CREATE TABLE "+TABLA_PLAYERS+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Nombre + " TEXT)";
    public static String CREATE_TABLE_BOSS = "CREATE TABLE "+TABLA_BOSS+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +Nombre+" TEXT, "+ Imagen + " TEXT, "+Categoria+ "TEXT, "+Hour+" TEXT);";
    public static String CREATE_TABLE_NOTIFICACIONES ="CREATE TABLE "+TABLA_NOTIFICACIONES+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +BossID+" TEXT, "+ PlayerID +" INTEGER, "+ Hour + " TEXT, " + Min + " TEXT, "+Imagen+" TEXT," +
            " "+timeSpan+" TEXT, "+Utilidades.HourMonster + " TEXT);";



    public static void IniciarAlarma(Context context)
    {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmSound == null) {
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            if(alarmSound == null)
            {
                alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
        }

        player = MediaPlayer.create(context, alarmSound);

        AudioManager audioManager= (AudioManager) context.getSystemService(AUDIO_SERVICE);
        int volumeLevel=audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        player.setVolume(volumeLevel,volumeLevel);
        player.start();
    }
}
