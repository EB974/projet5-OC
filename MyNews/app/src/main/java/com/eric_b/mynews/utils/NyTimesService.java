package com.eric_b.mynews.utils;

import com.eric_b.mynews.models.TopStoriePojo;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NyTimesService {

    @GET("svc/topstories/v2/{category}.json?api-key=571bed2a4b0d429380eca6b006c553d3")
    Observable<TopStoriePojo> getNews(@Path("category") String category);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
}
