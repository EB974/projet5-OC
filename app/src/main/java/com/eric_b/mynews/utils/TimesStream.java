package com.eric_b.mynews.utils;

import android.util.Log;

import com.eric_b.mynews.models.MostPopularPojo;
import com.eric_b.mynews.models.TopStoriePojo;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TimesStream {

    public static Observable<TopStoriePojo> streamFetchTopStorieNews(String category) {
        TopStorieService timesServiceTs = TopStorieService.retrofit.create(TopStorieService.class);
        return timesServiceTs.getNews(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<MostPopularPojo> streamFetchMostPopularNews() {
        MostPopularService timesServiceMp = MostPopularService.retrofit.create(MostPopularService.class);
        Log.e("repro","serviceMp "+timesServiceMp.getNews());
        return timesServiceMp.getNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
