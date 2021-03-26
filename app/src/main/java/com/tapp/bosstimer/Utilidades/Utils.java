package com.tapp.bosstimer.Utilidades;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.tapp.bosstimer.Helpers.AlarmReceiver;

import static android.content.Context.ALARM_SERVICE;

public class Utils {
    public static void setAlarm(int i, long timestamp, Context ctx, String characterName, String boss){
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(ctx, AlarmReceiver.class);
        alarmIntent.putExtra("CHAR",characterName);
        alarmIntent.putExtra("BOSS",boss);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(ctx, i, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp,pendingIntent);
    }
}
