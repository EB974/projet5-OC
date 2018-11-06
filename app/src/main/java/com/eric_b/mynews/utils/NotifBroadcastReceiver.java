package com.eric_b.mynews.utils;


import android.content.Context;
import android.content.Intent;
import android.os.Build;


public class NotifBroadcastReceiver extends android.content.BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent serviceIntent = new Intent(context, NotificationService.class);
        serviceIntent.putExtra("serviceInputTerm",intent.getStringExtra("inputTerms"));
        serviceIntent.putExtra("searchCategory",intent.getStringExtra("searchCategory"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent); // for build on Oreo and sup
        } else {
            context.startService(serviceIntent);
        }

    }

}
