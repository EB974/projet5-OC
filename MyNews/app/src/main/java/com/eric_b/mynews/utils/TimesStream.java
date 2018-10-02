package com.eric_b.mynews.utils;

import com.eric_b.mynews.models.TopStoriePojo;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TimesStream {

    public static Observable<TopStoriePojo> streamFetchNews(String news) {
        NyTimesService timesService = NyTimesService.retrofit.create(NyTimesService.class);
        return timesService.getNews(news)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
