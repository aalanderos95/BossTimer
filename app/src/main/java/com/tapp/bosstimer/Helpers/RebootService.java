package com.tapp.bosstimer.Helpers;

import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.tapp.bosstimer.R;
import com.tapp.bosstimer.Utilidades.Utils;

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
            Utils.setAlarm(settings.getInt("alarmID", 0), settings.getLong("alarmTime",0),this);
        }
    }
}
