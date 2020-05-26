package com.insightsurfface.joke.config;

import com.insightsurfface.joke.bean.JokeBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface HttpService {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/joke/content/text.php?key=" + Configures.JOKE_KEY)
    Observable<JokeBean> getJokes(@Query("page") int page, @Query("pagesize") int pagesize);
}