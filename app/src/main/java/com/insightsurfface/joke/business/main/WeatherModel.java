package com.insightsurfface.joke.business.main;

import android.content.Context;

import com.insightsurfface.joke.bean.CityBean;
import com.insightsurfface.joke.bean.WeatherBean;
import com.insightsurfface.joke.bean.WeatherTypeBean;
import com.insightsurfface.joke.config.Configures;
import com.insightsurfface.joke.okhttp.HttpService;
import com.insightsurfface.joke.utils.RetrofitUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
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
                .getWeatherTypes(Configures.WEATHER_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Function<WeatherTypeBean, ObservableSource<WeatherBean>>() {
                    @Override
                    public ObservableSource<WeatherBean> apply(WeatherTypeBean weatherTypeBean) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<WeatherBean>() {
                            @Override
                            public void subscribe(ObservableEmitter<WeatherBean> emitter) throws Exception {
                                RetrofitUtil.getInstance(mContext)
                                        .create(HttpService.class)
                                        .getWeather(city, Configures.WEATHER_KEY)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(Schedulers.io())
                                        .subscribe(new Observer<WeatherBean>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onNext(WeatherBean bean) {
                                                for (int i = 0; i < bean.getResult().getFuture().size(); i++) {
                                                    WeatherBean.ResultBean.FutureBean.WidBean item = bean.getResult().getFuture().get(i).getWid();
                                                    WeatherBean.ResultBean.FutureBean.WeatherExplainBean explainBean = new WeatherBean.ResultBean.FutureBean.WeatherExplainBean();
                                                    explainBean.setDay(getWeatherByWid(item.getDay(), weatherTypeBean));
                                                    explainBean.setNight(getWeatherByWid(item.getNight(), weatherTypeBean));
                                                    bean.getResult().getFuture().get(i).setwExplain(explainBean);
                                                    emitter.onNext(bean);
                                                }
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onComplete() {

                                            }
                                        });
                            }
                        });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private String getWeatherByWid(String id, WeatherTypeBean bean) {
        for (int i = 0; i < bean.getResult().size(); i++) {
            if (id.equals(bean.getResult().get(i).getWid())) {
                return bean.getResult().get(i).getWeather();
            }
        }
        return "";
    }
}
