package com.insightsurfface.joke.business.main;

import android.content.Context;

import com.insightsurfface.joke.base.BaseViewModel;
import com.insightsurfface.joke.bean.CityBean;
import com.insightsurfface.joke.bean.WeatherBean;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class WeatherViewModel extends BaseViewModel {
    private Context mContext;
    private IWeatherModel mWeatherModel;
    private MutableLiveData<CityBean> city = new MutableLiveData<>();

    public WeatherViewModel(Context context) {
        mContext = context.getApplicationContext();
        mWeatherModel = new WeatherModel(mContext);
    }

    public void getSupportCities() {
        isUpdating.setValue(true);
        DisposableObserver<CityBean> observer = new DisposableObserver<CityBean>() {
            @Override
            public void onNext(CityBean bean) {
                city.setValue(bean);
            }

            @Override
            public void onError(Throwable e) {
                isUpdating.setValue(false);
                message.setValue(e.getMessage());
            }

            @Override
            public void onComplete() {
                isUpdating.setValue(false);
            }
        };
        mObserver.add(observer);
        mWeatherModel.getSupportCities(observer);
    }

    public void getWeather(String city) {
        isUpdating.setValue(true);
        DisposableObserver<WeatherBean> observer = new DisposableObserver<WeatherBean>() {
            @Override
            public void onNext(WeatherBean bean) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        mObserver.add(observer);
        mWeatherModel.getWeather(city,observer);
    }

    public LiveData<CityBean> getCity() {
        return city;
    }

    public LiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }
}
