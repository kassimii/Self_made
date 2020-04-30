package com.example.self_made;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

class NotificationReceiver extends BroadcastReceiver {

    private final String CHANNEL_ID = "breakfast notification";
    private final int NOTIFICATION_ID = 001;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            CharSequence name = "Breakfast notification";
            String description = "show notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name, importance);
            notificationChannel.setDescription(description);

            notificationManager.createNotificationChannel(notificationChannel);
        }


        Intent analyze_photo_intent = new Intent(context, AnalyzePhoto.class);
        analyze_photo_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent =  PendingIntent.getActivity(context,NOTIFICATION_ID,analyze_photo_intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle("Don't forget to eat breakfast!")
                .setContentText("Analyze your breakfast ingredients")
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID,builder.build());

    }


}
