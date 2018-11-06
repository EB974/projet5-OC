package com.eric_b.mynews.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateAdapter {


    //private static String publishedDate;
    private static String mDate;
    //private static final String TAG = DateAdapter.class.getSimpleName();


    public static String getDateTopStories(String publishedDate){
        if (publishedDate != null) mDate = publishedDate;
        else {
            return mDate;
        }
        String mDay = mDate.substring(0,10);
        String format = "yyyy-MM-dd";
        @SuppressLint("SimpleDateFormat") java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat( format );
        java.util.Date date = new java.util.Date();
        String dayNewsDate = formater.format(date);
        if(dayNewsDate.equals(mDay)) mDay = "today";
        publishedDate = mDay+" "+mDate.substring(11,16);
        return publishedDate;
    }


    public static String getDateMostPopular(String publishedDate) {
        if (publishedDate != null) mDate = publishedDate;
        else {
            return mDate;
        }
        String mDay = mDate.substring(0,10);
        String format = "yyyy-MM-dd";
        @SuppressLint("SimpleDateFormat") java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(format);
        java.util.Date date = new java.util.Date();
        String dayNewsDate = formater.format(date);
        if (dayNewsDate.equals(mDay)) mDay = "today";
        return mDay;
    }

        static String today() { //set today date for notification
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd",Locale.FRENCH);
            Calendar today = Calendar.getInstance();
            return sdf.format(today.getTimeInMillis());
        }

        static String tomorrow() {//set tomorrow date for notification
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd",Locale.FRENCH);
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DATE, 1);
            return sdf.format(tomorrow.getTimeInMillis());
        }
}
