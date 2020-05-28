package com.insightsurfface.joke.okhttp;

import com.insightsurfface.joke.bean.CityBean;
import com.insightsurfface.joke.bean.JokeBean;
import com.insightsurfface.joke.bean.WeatherBean;
import com.insightsurfface.joke.bean.WeatherTypeBean;
import com.insightsurfface.joke.config.Configures;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HttpService {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/joke/content/text.php?key=" + Configures.JOKE_KEY)
    Observable<JokeBean> getJokes(@Query("page") int page, @Query("pagesize") int pagesize);

//    @FormUrlEncoded
//    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json"})
//    @POST("http://apis.juhe.cn/simpleWeather/wids")
//    Observable<WeatherTypeBean> getWeatherTypes(@Field("key") String key);
//
//    @FormUrlEncoded
//    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json"})
//    @POST("http://apis.juhe.cn/simpleWeather/query")
//    Observable<WeatherBean> getWeather(@Field("city") String city, @Field("key") String key);
//
//    @FormUrlEncoded
//    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json"})
//    @POST("http://apis.juhe.cn/simpleWeather/cityList")
//    Observable<CityBean> getSupportCities(@Field("key") String key);

    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json"})
    @GET(Configures.GET_WEATHER_TYPE_URL)
    Observable<WeatherTypeBean> getWeatherTypes(@Query("key") String key);

    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json"})
    @GET("http://apis.juhe.cn/simpleWeather/query")
    Observable<WeatherBean> getWeather(@Query("city") String city, @Query("key") String key);

    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json"})
    @GET(Configures.GET_CITY_LIST_URL)
    Observable<CityBean> getSupportCities(@Query("key") String key);
}
