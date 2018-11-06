package com.eric_b.mynews.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import com.eric_b.mynews.R;
import com.eric_b.mynews.controllers.activity.ResultActivity;
import com.eric_b.mynews.models.search.SearchPojo;
import io.reactivex.observers.DisposableObserver;


class NotificationService extends Service {


    private static final String CHANEL_ID = "NOTIFICATION";
    private static final int NOTIFICATION_ID = 5;
    DisposableObserver<SearchPojo> disposable;
    String inputTerms;
    String searchCategory;
    Context mContext;

    public NotificationService(){

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = getApplicationContext();
        inputTerms = intent.getStringExtra("serviceInputTerm");
        searchCategory = intent.getStringExtra("searchCategory");
        newsFound(searchCategory,inputTerms);
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void newsFound(String searchCat, String inputTerm) {
        Log.d("Notif","inputTerm "+inputTerm );
        disposable = TimesStream.streamFetchSearchNews("best", searchCat, inputTerm, DateAdapter.today(), DateAdapter.tomorrow()).subscribeWith(new DisposableObserver<SearchPojo>() {
            int numberArticle;
            @Override
            public void onNext(SearchPojo response) {
                numberArticle = response.getResponse().getMeta().getHits();
                notificationSend(numberArticle);
                disposable.dispose();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("Notif","erreur "+e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void notificationSend(int numberArticle) {
        NotificationManager notificationManager = (NotificationManager) mContext
                .getSystemService(NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(mContext, ResultActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra("title", "articles about " + inputTerms);
        notificationIntent.putExtra("inputTerm", inputTerms);
        notificationIntent.putExtra("searchCategory", searchCategory);
        notificationIntent.putExtra("dateBegin", DateAdapter.today());
        notificationIntent.putExtra("dateEnd", DateAdapter.tomorrow());
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //createNotification Chanel for Build version Oreo and more
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification";
            String description = "Include all notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANEL_ID, name, importance);
            notificationChannel.setDescription(description);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        //set notification before send
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANEL_ID);
        builder.setSmallIcon(R.drawable.notification)
                .setSound(alarmSound)
                .setContentTitle(numberArticle + " Articles about " + inputTerms)
                .setContentText("in " + searchCategory)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{200, 1000, 500, 1000, 400})
                .setAutoCancel(true);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);

        if (numberArticle > 0) { // display notification
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForeground(NOTIFICATION_ID, builder.build()); //for oreo and more
                stopForeground(false);
            }
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build()); // for other build
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                /* for oreo and more
                   must startForeground() after startForegroundService()
                   even if no article was found */
                startForeground(NOTIFICATION_ID, builder.build());
                stopForeground(true); // remove blank notification
            }
        }

    }


}
