package com.eric_b.mynews.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.eric_b.mynews.R;
import com.eric_b.mynews.controllers.activity.Notifications;
import com.eric_b.mynews.models.search.SearchResponse;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.support.v4.content.ContextCompat.createDeviceProtectedStorageContext;
import static android.support.v4.content.ContextCompat.getSystemService;


public class MyBroadcastReceiver extends BroadcastReceiver {


    private static final String CHANEL_ID = "NOTIFICATION";
    private static final int NOTIFICATION_ID = 005;

    @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Notif","MyBroadcastReceiver");
            Toast.makeText(context, "Alarm", Toast.LENGTH_LONG).show();
            String inputTerm = intent.getStringExtra("inputTerm");
            String searchCategory = intent.getStringExtra("searchCategory");
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(NOTIFICATION_SERVICE);
            Intent notificationIntent = new Intent(context, SearchResponse.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //createNotificationChane
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Personnal Notification";
                String description = "Include all personnal notification";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel notificationChannel = new NotificationChannel(CHANEL_ID, name, importance);
                notificationChannel.setDescription(description);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(notificationChannel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANEL_ID);
                        builder.setSmallIcon(R.drawable.notification)
                        .setContentTitle(inputTerm)
                        .setContentText(searchCategory)
                        .setContentIntent(pendingIntent)
                        .setVibrate(new long[]{200, 1000, 500, 1000, 400});
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
    }

    private void createNotificationChanel(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Personnal Notification";
            String description = "Include all personnal notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANEL_ID, name, importance);
            notificationChannel.setDescription(description);
            //NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_ID);
            //assert notificationManager != null;
            //notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    
}
