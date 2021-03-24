package com.tapp.bosstimer.Helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.tapp.bosstimer.MainActivity;

public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SERVICIO", "ALARM RECEIVED");
        Bundle bundles = intent.getExtras();
        String character = bundles.getString("CHAR");
        String boss = bundles.getString("BOSS");
        Intent service1 = new Intent(context, NotificationService.class);
        service1.putExtra("CHAR",character);
        service1.putExtra("BOSS",boss);
        service1.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        //ContextCompat.startForegroundService(context, service1);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(service1);
        }
        else
        {
            context.startService(service1);
        }
        /*Intent i = new Intent();
        i.setClassName(context.getPackageName(), MainActivity.class.getName());
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("ACCION","ABRIRMODAL");
        i.putExtra("CHAR",character);
        i.putExtra("BOSS",boss);
        context.startActivity(i);*/
        /*Intent service2 = new Intent(context, MainActivity.class);

        service2.putExtra("ACCION","ABRIRMODAL");
        service2.putExtra("CHAR",character);
        service2.putExtra("BOSS",boss);
        service2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(service2);*/
    }
}