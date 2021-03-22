package com.tapp.bosstimer.Helpers;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.tapp.bosstimer.MainActivity;
import com.tapp.bosstimer.R;

public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String MENSAJE = bundle.getString("MENSAJE");
        String TITLE = bundle.getString("TITLE");
        sendNotification(MENSAJE,TITLE);
    }

    private void sendNotification(String msg, String title) {
        Log.d("EVENTOS", "Preparing to send notification...: " + msg);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent,PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle(title).
                setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setContentIntent(contentIntent);

        alarmNotificationManager.notify(1, alamNotificationBuilder.build());


        Log.d("EVENTOS", "Notification sent.");
    }
}