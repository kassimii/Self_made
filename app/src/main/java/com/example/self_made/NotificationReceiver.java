package com.example.self_made;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent analyze_photo_intent = new Intent(context, AnalyzePhoto.class);
        analyze_photo_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent =  PendingIntent.getActivity(context,100,analyze_photo_intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("Don't forget to eat breakfast!")
                .setContentText("Analyze your breakfast ingredients")
                .setAutoCancel(true);

        notificationManager.notify(100,builder.build());

    }
}
