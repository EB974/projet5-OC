package com.eric_b.mynews.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import com.eric_b.mynews.R;
import com.eric_b.mynews.controllers.activity.ResultActivity;
import com.eric_b.mynews.models.search.SearchPojo;
import io.reactivex.observers.DisposableObserver;
import static android.content.Context.NOTIFICATION_SERVICE;



public class MyBroadcastReceiver extends BroadcastReceiver {


    private static final String CHANEL_ID = "NOTIFICATION";
    private static final int NOTIFICATION_ID = 5;
    DisposableObserver<SearchPojo> disposable;
    String inputTerms;
    String searchCategory;
    Context mContext;
    int notifNumber;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext=context;
        inputTerms = intent.getStringExtra("inputTerm");
        searchCategory = intent.getStringExtra("searchCategory");
        notifNumber  = intent.getIntExtra("notifNumber",1);
        newsFound(searchCategory, inputTerms);
    }

    public void newsFound(String searchCat, String inputTerm) {
        disposable = TimesStream.streamFetchSearchNews("best", searchCat, inputTerm, DateAdapter.today(), DateAdapter.tomorrow()).subscribeWith(new DisposableObserver<SearchPojo>() {
            @Override
            public void onNext(SearchPojo response) {
                if (response.getResponse().getMeta().getHits() > 0) {
                    int numberArticle = response.getResponse().getMeta().getHits();
                    notificationSend(numberArticle);
                    disposable.dispose();
                }
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
        notificationIntent.putExtra("inputTerm", inputTerms);
        notificationIntent.putExtra("searchCategory", searchCategory);
        notificationIntent.putExtra("dateBegin", DateAdapter.today());
        notificationIntent.putExtra("dateEnd", DateAdapter.tomorrow());
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //createNotification Chanel for Build version Oreo and more
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Personnal Notification";
            String description = "Include all personnal notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANEL_ID, name, importance);
            notificationChannel.setDescription(description);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        //send notifications
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANEL_ID);
        builder.setSmallIcon(R.drawable.notification)
                .setNumber(notifNumber)
                .setContentTitle(numberArticle+" articles about " + inputTerms)
                .setContentText("in " + searchCategory)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{200, 1000, 500, 1000, 400});
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

    }
}
