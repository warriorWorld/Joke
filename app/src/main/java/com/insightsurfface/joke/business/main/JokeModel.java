package com.insightsurfface.joke.business.main;

import com.insightsurfface.joke.bean.JokeBean;
import com.insightsurfface.joke.config.HttpService;
import com.insightsurfface.joke.utils.RetrofitUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class JokeModel implements IJokeModel {

    @Override
    public void getJokes(int page, DisposableObserver<JokeBean> observer) {
        RetrofitUtil.getInstance()
                .create(HttpService.class)
                .getJokes(page, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
