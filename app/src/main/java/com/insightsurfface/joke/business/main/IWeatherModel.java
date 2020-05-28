package com.insightsurfface.joke.business.main;

import com.insightsurfface.joke.bean.CityBean;
import com.insightsurfface.joke.bean.WeatherBean;
import com.insightsurfface.joke.bean.WeatherTypeBean;

import io.reactivex.observers.DisposableObserver;

public interface IWeatherModel {
    //可缓存
    void getSupportCities(DisposableObserver<CityBean> observer);

    //可缓存
    void getWeatherTypes(DisposableObserver<WeatherTypeBean> observer);

    void getWeather(String city,DisposableObserver<WeatherBean> observer);
}
