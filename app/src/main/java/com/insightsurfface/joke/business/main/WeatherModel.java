package com.insightsurfface.joke.business.main;

import android.content.Context;

import com.insightsurfface.joke.bean.CityBean;
import com.insightsurfface.joke.bean.WeatherBean;
import com.insightsurfface.joke.bean.WeatherTypeBean;
import com.insightsurfface.joke.config.Configures;
import com.insightsurfface.joke.okhttp.HttpService;
import com.insightsurfface.joke.utils.RetrofitUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class WeatherModel implements IWeatherModel {
    private Context mContext;

    public WeatherModel(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void getSupportCities(DisposableObserver<CityBean> observer) {
        RetrofitUtil.getInstance(mContext)
                .create(HttpService.class)
                .getSupportCities(Configures.WEATHER_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void getWeatherTypes(DisposableObserver<WeatherTypeBean> observer) {
        RetrofitUtil.getInstance(mContext)
                .create(HttpService.class)
                .getWeatherTypes(Configures.WEATHER_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void getWeather(String city, DisposableObserver<WeatherBean> observer) {
        RetrofitUtil.getInstance(mContext)
                .create(HttpService.class)
                .getWeather(city, Configures.WEATHER_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
