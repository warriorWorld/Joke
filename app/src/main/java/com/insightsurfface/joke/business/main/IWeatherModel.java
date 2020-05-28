package com.insightsurfface.joke.business.main;

public interface IWeatherModel {
    //可缓存
    void getSupportCities();

    //可缓存
    void getWeatherTypes();

    void getWeather();
}
