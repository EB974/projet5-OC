package com.eric_b.mynews.utils;

import com.eric_b.mynews.models.mostpopular.MostPopularPojo;
import com.eric_b.mynews.models.search.SearchPojo;
import com.eric_b.mynews.models.topstories.TopStoriePojo;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TimesStream {

    //retrieve article list from TopStories API
    public static Observable<TopStoriePojo> streamFetchTopStorieNews(String category) {
        TopStorieService timesServiceTs = TopStorieService.retrofit.create(TopStorieService.class);
        return timesServiceTs.getNews(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    //retrieve article list from MostPopular API
    public static Observable<MostPopularPojo> streamFetchMostPopularNews() {
        MostPopularService timesServiceMp = MostPopularService.retrofit.create(MostPopularService.class);
        return timesServiceMp.getNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    //retrieve article list from Search API
    public static Observable<SearchPojo> streamFetchSearchNews(String sort,String category, String term, String beginDate, String endDate) {
        SearchService timesServiceSc = SearchService.retrofit.create(SearchService.class);
        return timesServiceSc.getNews(sort,category,term,beginDate,endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
