package com.insightsurfface.joke.business.main;

import android.content.Context;

import com.insightsurfface.joke.bean.JokeBean;

import io.reactivex.observers.DisposableObserver;

public interface IJokeModel {
    void getJokes(Context context, int page, DisposableObserver<JokeBean> observer);
}
