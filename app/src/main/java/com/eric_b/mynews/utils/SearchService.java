package com.eric_b.mynews.utils;

import com.eric_b.mynews.models.search.SearchPojo;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchService {

    @GET("svc/search/v2/articlesearch.json?api-key=571bed2a4b0d429380eca6b006c553d3")
    Observable<SearchPojo> getNews(@Query("sort") String sort, @Query("fq") String category, @Query("q") String term, @Query("begin_date") String beginDate, @Query("end_date") String endDate);

    // set retrofit service
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
}
